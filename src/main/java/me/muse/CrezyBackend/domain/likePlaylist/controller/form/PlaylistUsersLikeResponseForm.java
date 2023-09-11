package me.muse.CrezyBackend.domain.likePlaylist.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.muse.CrezyBackend.domain.song.entity.Song;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PlaylistUsersLikeResponseForm {
    final private Long playlistId;
    final private String playlistName;
    final private String thumbnailName;
    final private String accountWriter;
    final private List<Song> songlist;
    final private int likers;

}

