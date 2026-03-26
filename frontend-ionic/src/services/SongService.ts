import axios from 'axios'

const BASE_URL = '/api/songs'

export interface Song {
  id?: number
  title: string
  artist: string
  genre: string
  mood: Mood
  notes: string
}

export type Mood = 'HAPPY' | 'SAD' | 'ENERGETIC' | 'RELAXED' | 'NOSTALGIC'

export const MOODS: { value: Mood; label: string; color: string }[] = [
  { value: 'HAPPY',     label: 'Feliz',      color: 'warning' },
  { value: 'SAD',       label: 'Triste',     color: 'primary' },
  { value: 'ENERGETIC', label: 'Energico',   color: 'danger'  },
  { value: 'RELAXED',   label: 'Relajado',   color: 'success' },
  { value: 'NOSTALGIC', label: 'Nostalgico', color: 'medium'  }
]

export const songService = {
  getAll: () => axios.get<Song[]>(BASE_URL),
  getByMood: (mood: Mood) => axios.get<Song[]>(`${BASE_URL}?mood=${mood}`),
  getById: (id: number) => axios.get<Song>(`${BASE_URL}/${id}`),
  create: (song: Song) => axios.post<Song>(BASE_URL, song),
  update: (id: number, song: Song) => axios.put<Song>(`${BASE_URL}/${id}`, song),
  delete: (id: number) => axios.delete(`${BASE_URL}/${id}`)
}