package example.meter.sound.soundmeter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import java.util.HashMap;
import java.util.List;

public class GraphView extends HorizontalScrollView {
    private static final String TAG = "GraphViewLibrary";
    public int canvasColor = Color.rgb(0, 0, 0);
    public double defaultWaveLength = 2.6d;
    public boolean drawFullGraph = false;
    public int graphColor = Color.rgb(255, 255, 255);
    public double graphXOffset = 0.75d;
    public int markerColor = Color.argb(160, 30, 30, 30);
    public Paint markerPaint;
    public int maxAmplitude = 30000;
    public volatile float move = 0.0f;
    public int needleColor = Color.rgb(Callback.DEFAULT_DRAG_ANIMATION_DURATION, 0, 0);
    public Paint needlePaint;
    public Paint paint;
    public boolean pausePlotting = false;
    public List<WaveSample> pointList;
    public int timeColor = Color.rgb(Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION);
    public int timeMarkerSize = 50;
    public int timeScale = 500;
    private Context context;
    private float f52x1;
    private float f53x2;
    private FrameLayout frame;
    private GraphSurfaceView graphSurfaceView;

    public GraphView(Context context) {
        super(context);
        init(context);
    }

    public GraphView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public GraphView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        this.frame = new FrameLayout(context);
        this.graphSurfaceView = new GraphSurfaceView(context);
        this.frame.addView(this.graphSurfaceView);
        setBackgroundColor(this.canvasColor);
        this.frame.setBackgroundColor(this.canvasColor);
        this.frame.setLayoutParams(new LayoutParams(-1, -1, 3));
        this.frame.requestLayout();
        addView(this.frame);
    }

    public int getMaxAmplitude() {
        return this.maxAmplitude;
    }

    public void setMaxAmplitude(int i) {
        this.maxAmplitude = i;
    }

    public int getTimeScale() {
        return this.timeScale;
    }

    public void setTimeScale(int i) {
        this.timeScale = i;
    }

    public double getGraphXOffset() {
        return this.graphXOffset;
    }

    public void setGraphXOffset(double d) {
        if (d > 0.0d && d < 1.0d) {
            this.graphXOffset = d;
        }
    }

    public void setCanvasColor(int i) {
        this.canvasColor = i;
        setBackgroundColor(i);
        this.frame.setBackgroundColor(i);
    }

    public void setMarkerColor(int i) {
        this.markerColor = i;
        this.markerPaint.setColor(i);
    }

    public void setGraphColor(int i) {
        this.graphColor = i;
        this.paint.setColor(i);
    }

    public void setTimeColor(int i) {
        this.timeColor = i;
    }

    public void setNeedleColor(int i) {
        this.needleColor = i;
        this.needlePaint.setColor(i);
    }

    public boolean isPaused() {
        return this.pausePlotting;
    }

    public void pause() {
        this.pausePlotting = true;
    }

    public void resume() {
        this.pausePlotting = false;
    }

    public void showFullGraph(List<WaveSample> list) {
        this.graphSurfaceView.setMasterList(list);
        this.graphSurfaceView.showFullGraph();
    }

    public void setMasterList(List<WaveSample> list) {
        this.graphSurfaceView.setMasterList(list);
    }

    public void startPlotting() {
        this.graphSurfaceView.startPlotting();
    }

    public void reset() {
        this.graphSurfaceView.resetDimensions();
    }

    public void stopPlotting() {
        this.graphSurfaceView.stopPlotting();
    }

    public void setWaveLengthPX(int i) {
        if (i < 2) {
            i = 2;
        }
        if (i > 15) {
            i = 15;
        }
        this.graphSurfaceView.setWaveLength(i);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.drawFullGraph) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            this.f52x1 = motionEvent.getX();
        } else if (action == 2) {
            this.f53x2 = motionEvent.getX();
            float f = this.f53x2;
            float f2 = f - this.f52x1;
            this.f52x1 = f;
            this.move += f2;
            this.graphSurfaceView.drawFullGraph();
        }
        return super.onTouchEvent(motionEvent);
    }

    public String formatTime(long j) {
        Object stringBuilder;
        int i = ((int) (j / 1000)) % 60;
        int i2 = (int) (j / 60000);
        StringBuilder stringBuilder2 = new StringBuilder();
        String str = "0";
        if (i2 < 10) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(str);
            stringBuilder3.append(i2);
            stringBuilder = stringBuilder3.toString();
        } else {
            stringBuilder = Integer.valueOf(i2);
        }
        stringBuilder2.append(stringBuilder);
        stringBuilder2.append(":");
        if (i < 10) {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(str);
            stringBuilder4.append(i);
            stringBuilder = stringBuilder4.toString();
        } else {
            stringBuilder = Integer.valueOf(i);
        }
        stringBuilder2.append(stringBuilder);
        return stringBuilder2.toString();
    }

    private class GraphSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
        public int deltaWidth;
        public int halfHeight;
        public volatile int waveLength;
        public int width;
        public int widthForFullGraph = 50;
        int freezCount = 0;
        int listMasterSize = 0;
        int redrawCount = 0;
        int sleepTime = 5;
        private Thread _plottingThread;
        private Context context;
        private int height;
        private SurfaceHolder holder;
        private volatile boolean isRunning = false;
        private volatile boolean stop = false;

        public GraphSurfaceView(Context context) {
            super(context);
            init(context);
        }

        public GraphSurfaceView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            init(context);
        }

        public GraphSurfaceView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            init(context);
        }

        public void setWaveLength(int i) {
            this.waveLength = i;
        }

        public void init(Context context) {
            this.context = context;
            setLayoutParams(new LayoutParams(500, 500));
            double d = (double) context.getResources().getDisplayMetrics().xdpi;
            double d2 = GraphView.this.defaultWaveLength * 5.0d;
            Double.isNaN(d);
            this.waveLength = (int) (d / d2);
            this.holder = getHolder();
            this.holder.addCallback(this);
            GraphView.this.paint = new Paint(1);
            GraphView.this.paint.setColor(GraphView.this.graphColor);
            GraphView.this.paint.setStrokeWidth(5.0f);
            GraphView.this.paint.setStyle(Style.STROKE);
            GraphView.this.needlePaint = new Paint(1);
            GraphView.this.needlePaint.setColor(GraphView.this.needleColor);
            GraphView.this.needlePaint.setStrokeWidth(5.0f);
            GraphView.this.needlePaint.setStyle(Style.STROKE);
            GraphView.this.markerPaint = new Paint();
            GraphView.this.markerPaint.setColor(GraphView.this.markerColor);
            GraphView.this.markerPaint.setStyle(Style.STROKE);
        }

        private void processAmplitude() {
            if (GraphView.this.pointList.size() != this.listMasterSize) {
                this.listMasterSize = GraphView.this.pointList.size();
                this.freezCount = -1;
                this.redrawCount = 0;
            } else {
                this.redrawCount++;
                if (this.redrawCount > this.waveLength) {
                    this.freezCount++;
                    int i = this.freezCount;
                    if (i > 0) {
                        this.sleepTime++;
                    } else if (i < 0) {
                        this.sleepTime--;
                        if (this.sleepTime < 0) {
                            this.sleepTime = 0;
                        }
                    }
                    this.redrawCount = this.waveLength;
                }
            }
            Path path = new Path();
            HashMap hashMap = new HashMap();
            double d = (double) this.width;
            double d2 = GraphView.this.graphXOffset;
            Double.isNaN(d);
            int i2 = (int) (d * d2);
            int size = GraphView.this.pointList.size() - 1;
            Path path2 = new Path();
            Paint paint = GraphView.this.markerPaint;
            double d3 = (double) this.width;
            double d4 = GraphView.this.graphXOffset;
            Double.isNaN(d3);
            d4 *= d3;
            Double.isNaN(d3);
            paint.setStrokeWidth((float) (d3 - d4));
            path2.moveTo((float) ((this.width / 8) + i2), 0.0f);
            path2.lineTo((float) ((this.width / 8) + i2), (float) this.height);
            Path path3 = new Path();
            size--;
            while (i2 >= 0 - this.waveLength) {
                if (size >= 0) {
                    if (size == 0) {
                        hashMap.put(Integer.valueOf(i2 - this.redrawCount), "00:00");
                    } else {
                        long time = ((WaveSample) GraphView.this.pointList.get(size)).getTime();
                        if (((WaveSample) GraphView.this.pointList.get(size - 1)).getTime() % ((long) GraphView.this.timeScale) > time % ((long) GraphView.this.timeScale)) {
                            hashMap.put(Integer.valueOf(i2 - this.redrawCount), GraphView.this.formatTime(time));
                        }
                    }
                    drawAmplitude((int) ((WaveSample) GraphView.this.pointList.get(size)).getAmplitude(), i2, path, path3);
                }
                size--;
                i2 -= this.waveLength;
            }
            renderAmplitude(hashMap, path, path2, path3);
        }

        private Path drawAmplitude(int i, int i2, Path path, Path path2) {
            int i3 = (this.halfHeight * i) / GraphView.this.maxAmplitude;
            double d = (double) this.width;
            double d2 = GraphView.this.graphXOffset;
            Double.isNaN(d);
            if (i2 == ((int) (d * d2))) {
                d = (double) this.width;
                d2 = GraphView.this.graphXOffset;
                Double.isNaN(d);
                path2.moveTo((float) (d * d2), (float) (this.halfHeight - i3));
                path2.lineTo((float) this.width, (float) (this.halfHeight - i3));
            }
            if (i3 > 0) {
                RectF rectF = new RectF();
                int i4 = i2 - this.redrawCount;
                rectF.set((float) i4, (float) (this.halfHeight - i3), (float) (i4 + (this.waveLength / 2)), (float) (this.halfHeight + i3));
                path.addArc(rectF, 180.0f, 180.0f);
                rectF.set((float) ((i2 - this.redrawCount) + (this.waveLength / 2)), (float) (this.halfHeight - i3), (float) ((i2 - this.redrawCount) + this.waveLength), (float) (this.halfHeight + i3));
                path.addArc(rectF, 0.0f, 180.0f);
            } else {
                path.moveTo((float) (i2 - this.redrawCount), (float) this.halfHeight);
                path.lineTo((float) ((i2 - this.redrawCount) + this.waveLength), (float) this.halfHeight);
            }
            return path;
        }

        public void renderAmplitude(HashMap<Integer, String> hashMap, Path path, Path path2, Path path3) {
            if (this.holder.getSurface().isValid()) {
                Canvas canvas = null;
                try {
                    canvas = this.holder.lockCanvas();
                    synchronized (this.holder) {
                        if (canvas != null) {
                            canvas.drawColor(GraphView.this.canvasColor);
                            for (Integer intValue : hashMap.keySet()) {
                                intValue.intValue();
                            }
                            canvas.drawPath(path, GraphView.this.paint);
                            if (path2 != null) {
                                canvas.drawPath(path2, GraphView.this.markerPaint);
                            }
                            if (path3 != null) {
                                canvas.drawPath(path3, GraphView.this.needlePaint);
                            }
                        }
                    }
                    if (canvas != null) {
                        this.holder.unlockCanvasAndPost(canvas);
                    }
                } catch (Throwable th) {
                    if (canvas != null) {
                        this.holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
            try {
                Thread.sleep((long) this.sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Log.d(GraphView.TAG, "Created");
            setLayoutParams(new LayoutParams(GraphView.this.getWidth(), GraphView.this.getHeight()));
            if (this.isRunning && !this._plottingThread.isAlive()) {
                startPlotting();
            }
        }

        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            Log.d(GraphView.TAG, "Changed");
            if (GraphView.this.drawFullGraph) {
                drawFullGraph();
            }
            reset();
        }

        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.d(GraphView.TAG, "Destroyed");
            this.stop = true;
            Thread thread = this._plottingThread;
            if (thread != null) {
                thread.interrupt();
            }
        }

        public void reset() {
            this.height = getHeight();
            this.halfHeight = this.height / 2;
            this.width = getWidth();
            if (this.holder.getSurface().isValid()) {
                Canvas canvas = null;
                try {
                    canvas = this.holder.lockCanvas();
                    synchronized (this.holder) {
                        if (canvas != null) {
                            canvas.drawColor(GraphView.this.canvasColor);
                        }
                    }
                    if (canvas != null) {
                        this.holder.unlockCanvasAndPost(canvas);
                    }
                } catch (Throwable th) {
                    if (canvas != null) {
                        this.holder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        public void setMasterList(List<WaveSample> list) {
            GraphView.this.pointList = list;
        }

        public void showFullGraph() {
            if (GraphView.this.pointList != null && GraphView.this.pointList.size() != 0) {
                GraphView.this.drawFullGraph = true;
                reset();
                this.stop = true;
                this.isRunning = false;
                Thread thread = this._plottingThread;
                if (thread != null) {
                    thread.interrupt();
                }
                this.widthForFullGraph = (GraphView.this.pointList.size() * this.waveLength) + 50;
                drawFullGraph();
            }
        }

        public void drawFullGraph() {
            new Thread(new Runnable() {
                public void run() {
                    int size;
                    GraphSurfaceView graphSurfaceView = GraphSurfaceView.this;
                    graphSurfaceView.deltaWidth = graphSurfaceView.width - GraphSurfaceView.this.widthForFullGraph;
                    if (GraphView.this.move > 0.0f) {
                        GraphView.this.move = 0.0f;
                    }
                    if (GraphSurfaceView.this.deltaWidth >= 0) {
                        GraphView.this.move = 0.0f;
                    } else if (GraphView.this.move < ((float) GraphSurfaceView.this.deltaWidth)) {
                        GraphView.this.move = (float) GraphSurfaceView.this.deltaWidth;
                    }
                    int i = 0;
                    if (GraphSurfaceView.this.widthForFullGraph < GraphSurfaceView.this.width) {
                        size = GraphView.this.pointList.size();
                    } else {
                        size = (int) ((((float) (GraphSurfaceView.this.width + GraphSurfaceView.this.waveLength)) + Math.abs(GraphView.this.move)) / ((float) GraphSurfaceView.this.waveLength));
                    }
                    if (size > GraphView.this.pointList.size()) {
                        size = GraphView.this.pointList.size();
                    }
                    HashMap hashMap = new HashMap();
                    Path path = new Path();
                    for (int abs = (int) (Math.abs(GraphView.this.move) / ((float) GraphSurfaceView.this.waveLength)); abs <= size - 1; abs++) {
                        if (abs == 0) {
                            hashMap.put(Integer.valueOf(i), "00:00");
                        } else {
                            long time = ((WaveSample) GraphView.this.pointList.get(abs)).getTime();
                            if (((WaveSample) GraphView.this.pointList.get(abs - 1)).getTime() % ((long) GraphView.this.timeScale) > time % ((long) GraphView.this.timeScale)) {
                                hashMap.put(Integer.valueOf(i), GraphView.this.formatTime(time));
                            }
                        }
                        int amplitude = (GraphSurfaceView.this.halfHeight * ((int) ((WaveSample) GraphView.this.pointList.get(abs)).getAmplitude())) / GraphView.this.maxAmplitude;
                        if (amplitude > 0) {
                            RectF rectF = new RectF();
                            rectF.set((float) i, (float) (GraphSurfaceView.this.halfHeight - amplitude), (float) ((GraphSurfaceView.this.waveLength / 2) + i), (float) (GraphSurfaceView.this.halfHeight + amplitude));
                            path.addArc(rectF, 180.0f, 180.0f);
                            rectF.set((float) ((GraphSurfaceView.this.waveLength / 2) + i), (float) (GraphSurfaceView.this.halfHeight - amplitude), (float) (GraphSurfaceView.this.waveLength + i), (float) (GraphSurfaceView.this.halfHeight + amplitude));
                            path.addArc(rectF, 0.0f, 180.0f);
                        } else {
                            path.moveTo((float) i, (float) GraphSurfaceView.this.halfHeight);
                            path.lineTo((float) (GraphSurfaceView.this.waveLength + i), (float) GraphSurfaceView.this.halfHeight);
                        }
                        i += GraphSurfaceView.this.waveLength;
                    }
                    GraphSurfaceView.this.renderAmplitude(hashMap, path, null, null);
                }
            }).start();
        }

        public void startPlotting() {
            GraphView.this.drawFullGraph = false;
            reset();
            this.stop = false;
            this.isRunning = true;
            this._plottingThread = new Thread(this);
            this._plottingThread.start();
        }

        public void stopPlotting() {
            this.stop = true;
            this.isRunning = false;
            Thread thread = this._plottingThread;
            if (thread != null) {
                thread.interrupt();
            }
        }

        public void run() {
            while (!this.stop) {
                if (!GraphView.this.pausePlotting) {
                    processAmplitude();
                }
            }
        }

        public void resetDimensions() {
            setLayoutParams(new LayoutParams(GraphView.this.getWidth(), GraphView.this.getHeight()));
        }
    }
}
