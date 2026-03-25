package com.musicmood.service;

import com.musicmood.model.Mood;
import com.musicmood.model.Song;
import com.musicmood.repository.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public List<Song> getSongsByMood(Mood mood) {
        return songRepository.findByMood(mood);
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada con id: " + id));
    }

    public Song createSong(Song song) {
        return songRepository.save(song);
    }

    public Song updateSong(Long id, Song updatedSong) {
        Song existing = getSongById(id);
        existing.setTitle(updatedSong.getTitle());
        existing.setArtist(updatedSong.getArtist());
        existing.setGenre(updatedSong.getGenre());
        existing.setMood(updatedSong.getMood());
        existing.setNotes(updatedSong.getNotes());
        return songRepository.save(existing);
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }
}