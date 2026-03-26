<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-title>MusicMood</ion-title>
      </ion-toolbar>
    </ion-header>

    <ion-content>
      <div class="mood-filter">
        <ion-chip
          :outline="activeMood !== null"
          @click="filterByMood(null)"
        >
          Todos
        </ion-chip>
        <ion-chip
          v-for="mood in MOODS"
          :key="mood.value"
          :outline="activeMood !== mood.value"
          :color="mood.color"
          @click="filterByMood(mood.value)"
        >
          {{ mood.label }}
        </ion-chip>
      </div>

      <ion-list v-if="songs.length > 0">
        <ion-item-sliding v-for="song in songs" :key="song.id">
          <ion-item>
            <ion-label>
              <h2>{{ song.title }}</h2>
              <p>{{ song.artist }}<span v-if="song.genre"> · {{ song.genre }}</span></p>
              <p v-if="song.notes" class="notes">{{ song.notes }}</p>
            </ion-label>
            <ion-badge slot="end" :color="getMoodColor(song.mood)">
              {{ getMoodLabel(song.mood) }}
            </ion-badge>
          </ion-item>

          <ion-item-options side="end">
            <ion-item-option color="primary" @click="editSong(song.id!)">
              <ion-icon :icon="createOutline" slot="icon-only" />
            </ion-item-option>
            <ion-item-option color="danger" @click="confirmDelete(song)">
              <ion-icon :icon="trashOutline" slot="icon-only" />
            </ion-item-option>
          </ion-item-options>
        </ion-item-sliding>
      </ion-list>

      <div v-else-if="!isLoading" class="empty-state">
        <ion-icon :icon="musicalNotesOutline" />
        <p>No hay canciones aun</p>
        <ion-button router-link="/song/new">Agregar una cancion</ion-button>
      </div>

      <ion-fab slot="fixed" vertical="bottom" horizontal="end">
        <ion-fab-button router-link="/song/new">
          <ion-icon :icon="addOutline" />
        </ion-fab-button>
      </ion-fab>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  IonPage, IonHeader, IonToolbar, IonTitle, IonContent,
  IonList, IonItem, IonItemSliding, IonItemOptions, IonItemOption,
  IonLabel, IonBadge, IonChip, IonButton, IonIcon,
  IonFab, IonFabButton,
  alertController, loadingController
} from '@ionic/vue'
import { addOutline, createOutline, trashOutline, musicalNotesOutline } from 'ionicons/icons'
import { songService, Song, Mood, MOODS } from '../services/SongService'

const router = useRouter()
const songs = ref<Song[]>([])
const isLoading = ref(false)
const activeMood = ref<Mood | null>(null)

const getMoodInfo = (mood: Mood) => MOODS.find(m => m.value === mood)
const getMoodLabel = (mood: Mood) => getMoodInfo(mood)?.label ?? mood
const getMoodColor = (mood: Mood) => getMoodInfo(mood)?.color ?? 'medium'

const loadSongs = async () => {
  isLoading.value = true
  const loading = await loadingController.create({ message: 'Cargando...' })
  await loading.present()
  try {
    const res = activeMood.value
      ? await songService.getByMood(activeMood.value)
      : await songService.getAll()
    songs.value = res.data
  } catch (e) {
    console.error('Error cargando canciones:', e)
  } finally {
    isLoading.value = false
    await loading.dismiss()
  }
}

const filterByMood = async (mood: Mood | null) => {
  activeMood.value = mood
  await loadSongs()
}

const editSong = (id: number) => {
  router.push(`/song/edit/${id}`)
}

const confirmDelete = async (song: Song) => {
  const alert = await alertController.create({
    header: 'Eliminar cancion',
    message: `Eliminar "${song.title}"?`,
    buttons: [
      { text: 'Cancelar', role: 'cancel' },
      {
        text: 'Eliminar',
        role: 'destructive',
        handler: async () => {
          await songService.delete(song.id!)
          await loadSongs()
        }
      }
    ]
  })
  await alert.present()
}

onMounted(loadSongs)
</script>

<style scoped>
.mood-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  padding: 12px 16px;
}
.notes {
  font-style: italic;
  color: var(--ion-color-medium);
}
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  gap: 16px;
  color: var(--ion-color-medium);
}
.empty-state ion-icon {
  font-size: 64px;
}
</style>