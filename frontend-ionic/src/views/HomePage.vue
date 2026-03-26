<template>
  <ion-page>
    <ion-header>
      <ion-toolbar color="primary">
        <ion-title> MusicMood</ion-title>
        <ion-buttons slot="end">
          <ion-button router-link="/song/new">
            <ion-icon :icon="addOutline" slot="icon-only" />
          </ion-button>
        </ion-buttons>
      </ion-toolbar>
    </ion-header>

    <ion-content>
      <!-- Filtro por mood -->
      <div class="mood-filter">
        <ion-chip
          :outline="activeMood !== null"
          color="primary"
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
          {{ mood.emoji }} {{ mood.label }}
        </ion-chip>
      </div>

      <!-- Lista de canciones -->
      <ion-list v-if="songs.length > 0">
        <ion-item-sliding v-for="song in songs" :key="song.id">
          <ion-item>
            <ion-label>
              <h2>{{ getMoodEmoji(song.mood) }} {{ song.title }}</h2>
              <p>{{ song.artist }} <span v-if="song.genre">· {{ song.genre }}</span></p>
              <p v-if="song.notes" class="notes">{{ song.notes }}</p>
            </ion-label>
            <ion-badge slot="end" :color="getMoodColor(song.mood)">
              {{ getMoodLabel(song.mood) }}
            </ion-badge>
          </ion-item>

          <!-- Opciones al deslizar -->
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

      <!-- Estado vacío -->
      <div v-else class="empty-state">
        <ion-icon :icon="musicalNotesOutline" />
        <p>No hay canciones aún</p>
        <ion-button router-link="/song/new">Agregar una canción</ion-button>
      </div>

      <!-- Loading -->
      <ion-loading :is-open="loading" message="Cargando..." />
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  IonPage, IonHeader, IonToolbar, IonTitle, IonContent,
  IonList, IonItem, IonItemSliding, IonItemOptions, IonItemOption,
  IonLabel, IonBadge, IonChip, IonButton, IonButtons, IonIcon,
  IonLoading, alertController
} from '@ionic/vue'
import { addOutline, createOutline, trashOutline, musicalNotesOutline } from 'ionicons/icons'
import { songService, Song, Mood, MOODS } from '../services/SongService'

const router = useRouter()
const songs = ref<Song[]>([])
const loading = ref(false)
const activeMood = ref<Mood | null>(null)

const getMoodInfo = (mood: Mood) => MOODS.find(m => m.value === mood)
const getMoodEmoji = (mood: Mood) => getMoodInfo(mood)?.emoji ?? ''
const getMoodLabel = (mood: Mood) => getMoodInfo(mood)?.label ?? mood
const getMoodColor = (mood: Mood) => getMoodInfo(mood)?.color ?? 'medium'

const loadSongs = async () => {
  loading.value = true
  try {
    const res = activeMood.value
      ? await songService.getByMood(activeMood.value)
      : await songService.getAll()
    songs.value = res.data
  } catch (e) {
    console.error('Error cargando canciones:', e)
  } finally {
    loading.value = false
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
    header: 'Eliminar canción',
    message: `¿Eliminar "${song.title}"?`,
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