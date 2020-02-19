package example.meter.sound.soundmeter;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyMediaRecorder {
    public boolean isRecording = false;
    public File myRecAudioFile;
    public List<WaveSample> pointList = new ArrayList();
    private GraphView graphView;
    private MediaRecorder mMediaRecorder;

    public boolean startPlotting(GraphView graphView) {
        if (graphView == null) {
            return false;
        }
        this.graphView = graphView;
        graphView.setMasterList(this.pointList);
        graphView.startPlotting();
        return true;
    }

    public List getSamples() {
        return this.pointList;
    }

    public float getMaxAmplitude() {
        MediaRecorder mediaRecorder = this.mMediaRecorder;
        if (mediaRecorder == null) {
            return 5.0f;
        }
        try {
            return (float) mediaRecorder.getMaxAmplitude();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public File getMyRecAudioFile() {
        return this.myRecAudioFile;
    }

    public void setMyRecAudioFile(File file) {
        this.myRecAudioFile = file;
    }

    public boolean startRecorder() {
        if (this.myRecAudioFile == null) {
            return false;
        }
        try {
            this.mMediaRecorder = new MediaRecorder();
            this.mMediaRecorder.setAudioSource(1);
            this.mMediaRecorder.setOutputFormat(1);
            this.mMediaRecorder.setAudioEncoder(1);
            this.mMediaRecorder.setOutputFile(this.myRecAudioFile.getAbsolutePath());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            this.isRecording = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            this.mMediaRecorder.reset();
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
            this.isRecording = false;
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e2) {
            stopRecording();
            e2.printStackTrace();
            this.isRecording = false;
            return false;
        }
    }

    public List stopRecording() {
        if (this.mMediaRecorder != null) {
            if (this.isRecording) {
                try {
                    if (this.graphView != null) {
                        this.graphView.stopPlotting();
                    }
                    this.mMediaRecorder.stop();
                    this.mMediaRecorder.reset();
                    this.mMediaRecorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.mMediaRecorder = null;
            this.isRecording = false;
        }
        return this.pointList;
    }

    public void release() {
        if (this.isRecording) {
            stopRecording();
        }
    }

    public List stopRecording_list() {
        if (this.mMediaRecorder != null) {
            if (this.isRecording) {
                try {
                    if (this.graphView != null) {
                        this.graphView.stopPlotting();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.isRecording = false;
        }
        return this.pointList;
    }

    public void delete() {
        stopRecording();
        File file = this.myRecAudioFile;
        if (file != null) {
            file.delete();
            this.myRecAudioFile = null;
        }
    }

    public void reset() {
        this.mMediaRecorder.reset();
    }
}
