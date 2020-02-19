package example.meter.sound.soundmeter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import example.meter.sound.R;

public class HistoryAdapter extends Adapter<HistoryAdapter.ViewHolder> {
    private static final String TAG = "HistoryAdapter";
    public DatabaseHelper f65db;
    public ArrayList<AudioDetails> mAudioDetailsArrayList;
    public int mColorArcProgressBarPlayingId = -1;
    public Context mContext;
    public AlertDialog mRenameAlertDialog;
    String[] guessSurrounding = new String[]{"Threshold of pain, Thunder", "Concerts, Screaming child", "Motorcycle, Blow dryer", "Diesel truck, Power mower", "Loud music, Alarm clocks", "Busy traffic, Vacuum cleaner", "Normal conversation at 3 ft.", "Quiet office, Moderate rainfall", "Quiet library, Bird calls", "Whisper, Quiet rural area", "Rustling leaves, Ticking watch", "Almost quiet, Breathing"};

    public HistoryAdapter(Context context, DatabaseHelper databaseHelper, ArrayList<AudioDetails> arrayList) {
        this.mContext = context;
        this.f65db = databaseHelper;
        this.mAudioDetailsArrayList = arrayList;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_list_item, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");
        Resources resources = this.mContext.getResources();
        AudioDetails audioDetails = (AudioDetails) this.mAudioDetailsArrayList.get(i);
        viewHolder.textViewAudioName.setText(String.format(resources.getString(R.string.name), new Object[]{audioDetails.getName()}));
        viewHolder.textViewAudioMin.setText(audioDetails.getMinValue());
        viewHolder.textViewAudioAvg.setText(audioDetails.getAvgValue());
        viewHolder.textViewAudioMax.setText(audioDetails.getMaxValue());
        viewHolder.textViewAudioDurationAndEnvironment.setText(String.format(resources.getString(R.string.duration_and_environment), new Object[]{audioDetails.getDuration(), this.guessSurrounding[audioDetails.getEnvironment()]}));
        viewHolder.txt_duration.setText(String.format(resources.getString(R.string.duration_and_environment1), new Object[]{audioDetails.getDuration(), this.guessSurrounding[audioDetails.getEnvironment()]}));
        viewHolder.includeViewAudioProgressBar.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                int adapterPosition = viewHolder.getAdapterPosition();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onClick: clicked on: ");
                stringBuilder.append(adapterPosition);
                String stringBuilder2 = stringBuilder.toString();
                String str = HistoryAdapter.TAG;
                Log.d(str, stringBuilder2);
                if (HistoryAdapter.this.mColorArcProgressBarPlayingId != adapterPosition) {
                    if (HistoryAdapter.this.mColorArcProgressBarPlayingId != -1) {
                        ActivityAudioHistory.mPlayerAdapter.reset();
                    }
                    ActivityAudioHistory.mPlayerAdapter.release();
                    HistoryAdapter.this.mColorArcProgressBarPlayingId = adapterPosition;
                    ActivityAudioHistory.mColorArcProgressBar = (ColorArcProgressBar1) view.findViewById(R.id.colorArcProgressBar);
                    ActivityAudioHistory.imageViewIncludeViewAudioProgressBar = (ImageView) view.findViewById(R.id.imageViewIncludeViewAudioProgressBar);
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("onClick: mMediaPlayer: ");
                    stringBuilder3.append(ActivityAudioHistory.mPlayerAdapter);
                    Log.d(str, stringBuilder3.toString());
                    ActivityAudioHistory.mPlayerAdapter.loadMedia(((AudioDetails) HistoryAdapter.this.mAudioDetailsArrayList.get(adapterPosition)).getName(), true);
                } else if (ActivityAudioHistory.mPlayerAdapter.isPlaying()) {
                    ActivityAudioHistory.mPlayerAdapter.pause();
                } else {
                    ActivityAudioHistory.mPlayerAdapter.play();
                }
            }
        });
    }

    public int getItemCount() {
        return this.mAudioDetailsArrayList.size();
    }

    public void clear() {
        int size = this.mAudioDetailsArrayList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mAudioDetailsArrayList.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout includeViewAudioProgressBar;
        LinearLayout linearLayoutHistoryListLayout;
        ImageView more_icon;
        TextView textViewAudioAvg;
        TextView textViewAudioDurationAndEnvironment;
        TextView textViewAudioMax;
        TextView textViewAudioMin;
        TextView textViewAudioName;
        TextView txt_duration;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.linearLayoutHistoryListLayout = (LinearLayout) view.findViewById(R.id.linearLayoutHistoryListLayout);
            this.textViewAudioName = (TextView) view.findViewById(R.id.textViewAudioName);
            this.includeViewAudioProgressBar = (RelativeLayout) view.findViewById(R.id.includeViewAudioProgressBar);
            this.textViewAudioMin = (TextView) view.findViewById(R.id.textViewAudioMin);
            this.textViewAudioAvg = (TextView) view.findViewById(R.id.textViewAudioAvg);
            this.textViewAudioMax = (TextView) view.findViewById(R.id.textViewAudioMax);
            this.more_icon = (ImageView) view.findViewById(R.id.more_icon);
            this.textViewAudioDurationAndEnvironment = (TextView) view.findViewById(R.id.textViewAudioDurationAndEnvironment);
            this.txt_duration = (TextView) view.findViewById(R.id.txt_duration);
            this.more_icon.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(HistoryAdapter.this.mContext, ViewHolder.this.more_icon);
                    popupMenu.inflate(R.menu.menu_options);
                    popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu1 /*2131362039*/:
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("onClick: clicked on: ");
                                    stringBuilder.append(ViewHolder.this.getAdapterPosition());
                                    String stringBuilder2 = stringBuilder.toString();
                                    String str = HistoryAdapter.TAG;
                                    Log.d(str, stringBuilder2);
                                    try {
                                        ActivityAudioHistory.mPlayerAdapter.pause();
                                        HistoryAdapter.this.mRenameAlertDialog = new Builder(HistoryAdapter.this.mContext).setView(R.layout.dialog_rename_history).setPositiveButton(R.string.yes, (DialogInterface.OnClickListener) null).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).setOnDismissListener(new OnDismissListener() {
                                            public void onDismiss(DialogInterface dialogInterface) {
                                            }
                                        }).create();
                                        HistoryAdapter.this.mRenameAlertDialog.setOnShowListener(new OnShowListener() {
                                            public void onShow(DialogInterface dialogInterface) {
                                                HistoryAdapter.this.mRenameAlertDialog.getButton(-1).setOnClickListener(new OnClickListener() {
                                                    @SuppressLint({"WrongConstant"})
                                                    public void onClick(View view) {
                                                        String name = ((AudioDetails) HistoryAdapter.this.mAudioDetailsArrayList.get(ViewHolder.this.getAdapterPosition())).getName();
                                                        EditText editText = (EditText) HistoryAdapter.this.mRenameAlertDialog.findViewById(R.id.newNameEditText);
                                                        String trim = editText.getText().toString().trim();
                                                        if (trim.equalsIgnoreCase("")) {
                                                            editText.setError("Name cannot be empty.");
                                                        } else if (name.equals(trim)) {
                                                            HistoryAdapter.this.mRenameAlertDialog.dismiss();
                                                        } else {
                                                            File filesDir = HistoryAdapter.this.mContext.getFilesDir();
                                                            StringBuilder stringBuilder = new StringBuilder();
                                                            stringBuilder.append(name);
                                                            name = ".amr";
                                                            stringBuilder.append(name);
                                                            File file = new File(filesDir, stringBuilder.toString());
                                                            filesDir = HistoryAdapter.this.mContext.getFilesDir();
                                                            stringBuilder = new StringBuilder();
                                                            stringBuilder.append(trim);
                                                            stringBuilder.append(name);
                                                            File file2 = new File(filesDir, stringBuilder.toString());
                                                            if (file2.exists()) {
                                                                editText.setError("File with same name already exists");
                                                                return;
                                                            }
                                                            boolean renameTo = file.renameTo(file2);
                                                            String str = HistoryAdapter.TAG;
                                                            Log.d(str, "File renamed successfully");
                                                            stringBuilder = new StringBuilder();
                                                            stringBuilder.append("onClick: dirName: ");
                                                            stringBuilder.append(HistoryAdapter.this.mContext.getFilesDir().getName());
                                                            Log.d(str, stringBuilder.toString());
                                                            stringBuilder = new StringBuilder();
                                                            stringBuilder.append("onClick: old file name: ");
                                                            stringBuilder.append(file.getName());
                                                            Log.d(str, stringBuilder.toString());
                                                            stringBuilder = new StringBuilder();
                                                            stringBuilder.append("onClick: new file name: ");
                                                            stringBuilder.append(file2.getName());
                                                            Log.d(str, stringBuilder.toString());
                                                            StringBuilder stringBuilder2 = new StringBuilder();
                                                            stringBuilder2.append("onClick: isRenamed: ");
                                                            stringBuilder2.append(renameTo);
                                                            Log.d(str, stringBuilder2.toString());
                                                            if (renameTo) {
                                                                if (HistoryAdapter.this.f65db.updateData(((AudioDetails) HistoryAdapter.this.mAudioDetailsArrayList.get(ViewHolder.this.getAdapterPosition())).getId(), trim)) {
                                                                    Log.d(str, "onClick: successfully renamed in Database");
                                                                } else {
                                                                    Log.e(str, "onClick: Failed to rename in Database!");
                                                                }
                                                                AudioDetails audioDetails = (AudioDetails) HistoryAdapter.this.mAudioDetailsArrayList.get(ViewHolder.this.getAdapterPosition());
                                                                audioDetails.setName(trim);
                                                                HistoryAdapter.this.mAudioDetailsArrayList.set(ViewHolder.this.getAdapterPosition(), audioDetails);
                                                                if (HistoryAdapter.this.mColorArcProgressBarPlayingId == ViewHolder.this.getAdapterPosition()) {
                                                                    HistoryAdapter.this.mColorArcProgressBarPlayingId = -1;
                                                                }
                                                                HistoryAdapter.this.notifyItemChanged(ViewHolder.this.getAdapterPosition());
                                                                HistoryAdapter.this.mRenameAlertDialog.dismiss();
                                                                return;
                                                            }
                                                            Log.d(str, "File not renamed!");
                                                            Toast.makeText(HistoryAdapter.this.mContext, "File can not be renamed!", 1).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        HistoryAdapter.this.mRenameAlertDialog.show();
                                        EditText editText = (EditText) HistoryAdapter.this.mRenameAlertDialog.findViewById(R.id.newNameEditText);
                                        editText.setHint(((AudioDetails) HistoryAdapter.this.mAudioDetailsArrayList.get(ViewHolder.this.getAdapterPosition())).getName());
                                        editText.setText(((AudioDetails) HistoryAdapter.this.mAudioDetailsArrayList.get(ViewHolder.this.getAdapterPosition())).getName(), BufferType.EDITABLE);
                                        break;
                                    } catch (Exception unused) {
                                        Log.e(str, "Something went wrong while renaming!");
                                        break;
                                    }
                                case R.id.menu2 /*2131362040*/:
                                    try {
                                        ActivityAudioHistory.mPlayerAdapter.pause();
                                        new Builder(HistoryAdapter.this.mContext).setView(R.layout.dialog_delete_history).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                File filesDir = HistoryAdapter.this.mContext.getFilesDir();
                                                StringBuilder stringBuilder = new StringBuilder();
                                                stringBuilder.append(((AudioDetails) HistoryAdapter.this.mAudioDetailsArrayList.get(ViewHolder.this.getAdapterPosition())).getName());
                                                stringBuilder.append(".amr");
                                                File file = new File(filesDir, stringBuilder.toString());
                                                boolean delete = file.delete();
                                                stringBuilder = new StringBuilder();
                                                stringBuilder.append("onClick: dirName: ");
                                                stringBuilder.append(HistoryAdapter.this.mContext.getFilesDir().getName());
                                                String stringBuilder2 = stringBuilder.toString();
                                                String str = HistoryAdapter.TAG;
                                                Log.d(str, stringBuilder2);
                                                stringBuilder = new StringBuilder();
                                                stringBuilder.append("onClick: fileName: ");
                                                stringBuilder.append(file.getName());
                                                Log.d(str, stringBuilder.toString());
                                                stringBuilder = new StringBuilder();
                                                stringBuilder.append("onClick: isDeleted: ");
                                                stringBuilder.append(delete);
                                                Log.d(str, stringBuilder.toString());
                                                if (delete) {
                                                    if (HistoryAdapter.this.f65db.deleteData(((AudioDetails) HistoryAdapter.this.mAudioDetailsArrayList.get(ViewHolder.this.getAdapterPosition())).getId()) == 0) {
                                                        Log.e(str, "onClick: Failed to delete from Database!");
                                                    } else {
                                                        Log.d(str, "onClick: Successfully deleted record from Database");
                                                    }
                                                    HistoryAdapter.this.mAudioDetailsArrayList.remove(ViewHolder.this.getAdapterPosition());
                                                    if (HistoryAdapter.this.mColorArcProgressBarPlayingId == ViewHolder.this.getAdapterPosition()) {
                                                        HistoryAdapter.this.mColorArcProgressBarPlayingId = -1;
                                                    } else if (ViewHolder.this.getAdapterPosition() < HistoryAdapter.this.mColorArcProgressBarPlayingId) {
                                                        HistoryAdapter.this.mColorArcProgressBarPlayingId--;
                                                    }
                                                    HistoryAdapter.this.notifyItemRemoved(ViewHolder.this.getAdapterPosition());
                                                    return;
                                                }
                                                Toast.makeText(HistoryAdapter.this.mContext, "File can not be deleted!", Toast.LENGTH_LONG).show();
                                                dialogInterface.dismiss();
                                            }
                                        }).setNegativeButton(R.string.no, (DialogInterface.OnClickListener) null).setOnDismissListener(new OnDismissListener() {
                                            public void onDismiss(DialogInterface dialogInterface) {
                                            }
                                        }).create().show();
                                        break;
                                    } catch (Exception unused2) {
                                        break;
                                    }
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }
}
