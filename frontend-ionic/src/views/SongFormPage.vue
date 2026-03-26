<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-buttons slot="start">
          <ion-back-button default-href="/home" />
        </ion-buttons>
        <ion-title>{{ isEditing ? 'Editar cancion' : 'Nueva cancion' }}</ion-title>
      </ion-toolbar>
    </ion-header>

    <ion-content class="ion-padding">
      <ion-list>
        <ion-item>
          <ion-label position="floating">Titulo *</ion-label>
          <ion-input v-model="form.title" placeholder="Nombre de la cancion" />
        </ion-item>
        <ion-item>
          <ion-label position="floating">Artista *</ion-label>
          <ion-input v-model="form.artist" placeholder="Nombre del artista" />
        </ion-item>
        <ion-item>
          <ion-label position="floating">Genero</ion-label>
          <ion-input v-model="form.genre" placeholder="Pop, Rock, Jazz..." />
        </ion-item>
        <ion-item>
          <ion-label position="floating">Notas</ion-label>
          <ion-textarea v-model="form.notes" placeholder="Que te hace sentir esta cancion?" :rows="3" />
        </ion-item>
      </ion-list>

      <div class="mood-section">
        <p class="mood-title">Mood *</p>
        <div class="mood-grid">
          <div
            v-for="mood in MOODS"
            :key="mood.value"
            class="mood-option"
            :class="{ selected: form.mood === mood.value }"
            @click="form.mood = mood.value"
          >
            <span class="mood-label">{{ mood.label }}</span>
          </div>
        </div>
      </div>

      <ion-button
        expand="block"
        class="ion-margin-top"
        :disabled="!isFormValid"
        @click="saveSong"
      >
        {{ isEditing ? 'Guardar cambios' : 'Agregar cancion' }}
      </ion-button>
    </ion-content>
  </ion-page>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  IonPage, IonHeader, IonToolbar, IonTitle, IonContent,
  IonList, IonItem, IonLabel, IonInput, IonTextarea,
  IonButton, IonButtons, IonBackButton,
  toastController, loadingController
} from '@ionic/vue'
import { songService, Song, Mood, MOODS } from '../services/SongService'

const route = useRoute()
const router = useRouter()

const form = ref<Song>({
  title: '',
  artist: '',
  genre: '',
  mood: 'HAPPY' as Mood,
  notes: ''
})

const isEditing = computed(() => !!route.params.id)
const isFormValid = computed(() => form.value.title.trim() && form.value.artist.trim() && form.value.mood)

const showToast = async (message: string, color = 'success') => {
  const toast = await toastController.create({ message, duration: 2000, color, position: 'bottom' })
  await toast.present()
}

const saveSong = async () => {
  if (!isFormValid.value) return
  const loading = await loadingController.create({ message: 'Guardando...' })
  await loading.present()
  try {
    if (isEditing.value) {
      await songService.update(Number(route.params.id), form.value)
      await showToast('Cancion actualizada')
    } else {
      await songService.create(form.value)
      await showToast('Cancion agregada')
    }
    router.push('/home')
  } catch (e) {
    await showToast('Ocurrio un error', 'danger')
    console.error(e)
  } finally {
    await loading.dismiss()
  }
}

onMounted(async () => {
  if (isEditing.value) {
    const loading = await loadingController.create({ message: 'Cargando...' })
    await loading.present()
    try {
      const res = await songService.getById(Number(route.params.id))
      form.value = res.data
    } finally {
      await loading.dismiss()
    }
  }
})
</script>

<style scoped>
.mood-section { padding: 16px 0; }
.mood-title {
  font-weight: 600;
  margin-bottom: 12px;
  padding: 0 4px;
}
.mood-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 8px;
}
.mood-option {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 14px 4px;
  border-radius: 12px;
  border: 2px solid var(--ion-color-light-shade);
  cursor: pointer;
  transition: all 0.2s;
}
.mood-option.selected {
  border-color: var(--ion-color-primary);
  background: var(--ion-color-primary-tint);
}
.mood-label {
  font-size: 12px;
  font-weight: 500;
  text-align: center;
  color: var(--ion-color-medium);
}
.mood-option.selected .mood-label {
  color: var(--ion-color-primary);
  font-weight: 700;
}
</style>