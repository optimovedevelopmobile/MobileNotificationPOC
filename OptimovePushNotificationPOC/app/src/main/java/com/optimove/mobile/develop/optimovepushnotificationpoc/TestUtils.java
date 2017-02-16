package com.optimove.mobile.develop.optimovepushnotificationpoc;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by yossi_c on 17/1/2017.
 */

public class TestUtils {


    public static class DownloadingTaskParams{

        String _url;
        Context _context;
    }


    public static  class DownloadFilesTask extends AsyncTask<DownloadingTaskParams, Integer, Bitmap> {


        @Override
        protected Bitmap doInBackground(DownloadingTaskParams... downloadingTaskParamses) {
            DownloadingTaskParams parm = downloadingTaskParamses[0];

            Bitmap createdBMP = null;
            try {
                createdBMP = Picasso.with(parm._context).load(parm._url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return createdBMP;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
