package me.muse.CrezyBackend.domain.likePlaylist.Service;

import me.muse.CrezyBackend.domain.likePlaylist.controller.form.PlaylistUsersLikeResponseForm;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface LikePlaylistService {
    int likePlaylist(Long playlistId, HttpHeaders headers);
    boolean isPlaylistLiked(Long playlistId, HttpHeaders headers);
    int unlikePlaylist(Long playlistId, HttpHeaders headers);
    List<PlaylistUsersLikeResponseForm> bringLikePlaylist(HttpHeaders headers);
}
