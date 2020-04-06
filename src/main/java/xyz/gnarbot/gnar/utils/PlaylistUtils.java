package xyz.gnarbot.gnar.utils;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageInput;
import com.sedmelluq.discord.lavaplayer.tools.io.MessageOutput;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.BasicAudioPlaylist;
import xyz.gnarbot.gnar.Bot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlaylistUtils {
    public static BasicAudioPlaylist decodePlaylist(List<String> encodedTracks, String name) {
        List<AudioTrack> tracks = new CopyOnWriteArrayList<>();

        for(String encodedTrack : encodedTracks) {
            try {
                tracks.add(toAudioTrack(encodedTrack));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new BasicAudioPlaylist(name, tracks, tracks.get(0), false);
    }

    public static List<String> encodePlaylist(BasicAudioPlaylist playlist) {
        List<AudioTrack> tracks = playlist.getTracks();
        List<String> encodedPlaylist = new CopyOnWriteArrayList<>();

        for(AudioTrack track : tracks) {
            try {
                encodedPlaylist.add(toBase64String(track));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return encodedPlaylist;
    }

    public static AudioTrack toAudioTrack(String encoded) throws IOException {
        AudioPlayerManager playerManager = Bot.getInstance().getPlayers().getPlayerManager();
        byte[] b64 = Base64.getDecoder().decode(encoded);
        ByteArrayInputStream bais = new ByteArrayInputStream(b64);
        return playerManager.decodeTrack(new MessageInput(bais)).decodedTrack;
    }

    public static String toBase64String(AudioTrack track) throws IOException {
        AudioPlayerManager playerManager = Bot.getInstance().getPlayers().getPlayerManager();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        playerManager.encodeTrack(new MessageOutput(baos), track);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}