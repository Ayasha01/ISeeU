package com.ayashasiddiqua.iseeu.views;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ayashasiddiqua.iseeu.services.PersistenceStreamService;
import com.ayashasiddiqua.iseeu.utils.Utils;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private String SCOPE_TAG = getClass().getCanonicalName();

    private SurfaceHolder surfaceHolder;
    private Context context;

    public CameraView(Context context) {
        super(context);

        this.context = context;
        init();
    }

    public void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

        doOnStart();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(SCOPE_TAG + " onCreated", "Called");

        doOnStart();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(SCOPE_TAG + " onChanged", "Called");

        doOnStart();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(SCOPE_TAG + " onDestroyed", "Called");
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.d(SCOPE_TAG + " Byte Received := ", "" + data.length);

        data = Utils.frameByteToJpegByte(data, camera);
        if (data != null) {
            MainActivity.iSeeUClient.send(data);
        }
    }

    public void doOnStart() {
        try {
            MainActivity.camera.setPreviewDisplay(surfaceHolder);
            MainActivity.camera.setPreviewCallback(this);
            MainActivity.camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doOnStop() {
        Log.d("doOnStop", "Called");

        getHolder().removeCallback(this);
        MainActivity.camera.stopPreview();

        if (!MainActivity.isQuit) {
            Intent streamService = new Intent(context, PersistenceStreamService.class);
            context.startService(streamService);
        }
    }

}
