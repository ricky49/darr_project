package ricky.darr.core.webservice_controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by JoanGabriel on 12/10/2014.
 */
public class TxnThread {

    private Activity ctx;
    private ProgressDialog dialog;
    private ExecutorService thread;
    private Runnable runnable;

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public TxnThread(final Activity ctx, final boolean cancelable, final boolean cancelableButton) {
        this.ctx= ctx;
        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = new ProgressDialog(ctx);
                dialog.setCancelable(cancelable);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        thread = Executors.newCachedThreadPool();
                        thread.submit(runnable);
                    }
                });

                if (cancelableButton) {
                    // Set cancelable button
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            TxnThread.this.close();
                        }
                    });
                }
            }
        });
    }

    public void close() {
        dialog.dismiss();
        thread.shutdown();
    }

    public void setText(final String text, final String ... params) {
        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setMessage(String.format(text, params));
            }
        });
    }

    public void setTitle(final String title) {
        ctx.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.setTitle(title);
            }
        });
    }

    public void show() {
        dialog.show();
    }
}
