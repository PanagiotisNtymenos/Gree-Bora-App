package com.example.greeboraapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class BlindModeActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    ObjectAnimator textColorAnim;
    TextToSpeech TTS;
    String sentenceToSay;
    boolean on = false;
    ArrayList<String> command;
    ArrayList<String> grades = new ArrayList<>();
    List<String> uniques = Arrays.asList("μηδέν", "έναν", "ένα", "δύο", "τρεις", "τέσσερις", "πέντε", "έξι", "εφτά", "οκτώ",
            "οχτώ", "εννέα", "εννιά", "δέκα", "ένδεκα", "έντεκα", "δώδεκα", "δεκατρείς", "δεκατέσσερις", "δεκαπέντε");
    int modeChoice = -1, swingChoice = -1, fanChoice = -1;
    private GestureDetector gesture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blind_mode);

        grades.addAll(uniques);

        gesture = new GestureDetector(new SwipeGestureDetector());
        TextView txt = findViewById(R.id.swipeLeft);

        textColorAnim = ObjectAnimator.ofInt(txt, "textColor", Color.BLACK, Color.TRANSPARENT);
        textColorAnim.setDuration(1000);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    command = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                }
                break;
        }

        if (command != null) {
            TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        Locale localeToUse = new Locale("el_GR");
                        TTS.setPitch((float) 0.8);
                        TTS.setLanguage(localeToUse);
                    }
                    if (command.get(0).toLowerCase().contains("ενεργοποίηση") || command.get(0).toLowerCase().contains("άνοιξε")) {
                        if (!on) {
                            sentenceToSay = "Το κλιματιστικό ενεργοποιήθηκε.";
                            TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                            on = true;
                        } else {
                            sentenceToSay = "Το κλιματιστικό βρίσκεται ήδη σε λειτουργία.";
                            TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                        }
                    } else if (command.get(0).toLowerCase().contains("απενεργοποίηση") || command.get(0).toLowerCase().contains("κλείσε")) {
                        if (on) {
                            sentenceToSay = "Το κλιματιστικό απενεργοποιήθηκε.";
                            TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                            on = false;
                        } else {
                            sentenceToSay = "Το κλιματιστικό βρίσκεται ήδη εκτός λειτουργίας.";
                            TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                        }
                    } else if (command.get(0).toLowerCase().contains("αύξησε") || command.get(0).toLowerCase().contains("ανέβασε") || command.get(0).toLowerCase().contains("αύξηση") || command.get(0).toLowerCase().contains("πάνω") || command.get(0).toLowerCase().contains("ανέβα")) {
                        ArrayList<String> st = new ArrayList<>();
                        int temp = 0;
                        String tempStr = "";
                        for (int i = 0; i < command.get(0).split(" ").length; i++) {
                            st.add(command.get(0).split(" ")[i]);
                        }
                        for (int i = 0; i < st.size(); i++) {
                            try {
                                temp = Integer.parseInt(st.get(i));
                                break;
                            } catch (Exception e) {
                            }
                            if (grades.contains(st.get(i))) {
                                tempStr = grades.get(i);
                                break;
                            }
                        }
                        if (on) {
                            if (!tempStr.equals("")) {
                                if (tempStr.equalsIgnoreCase("μηδέν")) {
                                    sentenceToSay = "Δεν υπήρξε μεταβολή στην θερμοκρασία.";
                                } else if (tempStr.equalsIgnoreCase("ένα") || tempStr.equalsIgnoreCase("έναν")) {
                                    sentenceToSay = "Η θερμοκρασία αυξήθηκε κατά " + tempStr + " βαθμό Κελσίου.";
                                } else {
                                    sentenceToSay = "Η θερμοκρασία αυξήθηκε κατά " + tempStr + " βαθμούς Κελσίου.";
                                }
                            } else {
                                if (temp == 0) {
                                    sentenceToSay = "Δεν υπήρξε μεταβολή στην θερμοκρασία.";
                                } else if (temp == 1) {
                                    sentenceToSay = "Η θερμοκρασία αυξήθηκε κατά " + temp + " βαθμό Κελσίου.";
                                } else {
                                    sentenceToSay = "Η θερμοκρασία αυξήθηκε κατά " + temp + " βαθμούς Κελσίου.";
                                }
                            }
                            TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                        }
                    } else if (command.get(0).toLowerCase().contains("μείωσε") || command.get(0).toLowerCase().contains("κατέβασε") || command.get(0).toLowerCase().contains("μείωση") || command.get(0).toLowerCase().contains("κάτω") || command.get(0).toLowerCase().contains("κατέβα")) {
                        ArrayList<String> st = new ArrayList<>();
                        int temp = 0;
                        String tempStr = "";
                        for (int i = 0; i < command.get(0).split(" ").length; i++) {
                            st.add(command.get(0).split(" ")[i]);
                        }
                        for (int i = 0; i < st.size(); i++) {
                            try {
                                temp = Integer.parseInt(st.get(i));
                                break;
                            } catch (Exception e) {
                            }
                            if (grades.contains(st.get(i))) {
                                tempStr = grades.get(i);
                                break;
                            }
                        }
                        if (on) {
                            if (!tempStr.equals("")) {
                                if (tempStr.equalsIgnoreCase("μηδέν")) {
                                    sentenceToSay = "Δεν υπήρξε μεταβολή στην θερμοκρασία.";
                                } else if (tempStr.equalsIgnoreCase("ένα") || tempStr.equalsIgnoreCase("έναν")) {
                                    sentenceToSay = "Η θερμοκρασία μειώθηκε κατά " + tempStr + " βαθμό Κελσίου.";
                                } else {
                                    sentenceToSay = "Η θερμοκρασία μειώθηκε κατά " + tempStr + " βαθμούς Κελσίου.";
                                }
                            } else {
                                if (temp == 0) {
                                    sentenceToSay = "Δεν υπήρξε μεταβολή στην θερμοκρασία.";
                                } else if (temp == 1) {
                                    sentenceToSay = "Η θερμοκρασία μειώθηκε κατά " + temp + " βαθμό Κελσίου.";
                                } else {
                                    sentenceToSay = "Η θερμοκρασία μειώθηκε κατά " + temp + " βαθμούς Κελσίου.";
                                }
                            }
                            TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                        }
                    } else if (command.get(0).toLowerCase().contains("λειτουργία")) {
                        if (on) {
                            if (command.get(0).toLowerCase().contains("αυτόματη")) {
                                if (modeChoice != 0) {
                                    sentenceToSay = "Αυτόματη λειτουργία ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    modeChoice = 0;
                                } else {
                                    sentenceToSay = "Η αυτόματη λειτουργία είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("ψυχρή")) {
                                if (modeChoice != 1) {
                                    sentenceToSay = "Ψυχρή λειτουργία ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    modeChoice = 1;
                                } else {
                                    sentenceToSay = "Η ψυχρή λειτουργία είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("αφύγρανση")) {
                                if (modeChoice != 2) {
                                    sentenceToSay = "Λειτουργία αφύγρανσης ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    modeChoice = 2;
                                } else {
                                    sentenceToSay = "Η λειτουργία αφύγρανσης είναι ηδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("ανεμιστήρα")) {
                                if (modeChoice != 3) {
                                    sentenceToSay = "Λειτουργία ανεμιστήρα ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    modeChoice = 3;
                                } else {
                                    sentenceToSay = "Η λειτουργία ανεμιστήρα είναι ηδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("θερμή")) {
                                if (modeChoice != 4) {
                                    sentenceToSay = "Θερμή λειτουργία ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    modeChoice = 4;
                                } else {
                                    sentenceToSay = "Η θερμή λειτουργία είναι ηδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            }
                        }
                    } else if (command.get(0).toLowerCase().contains("ανάκλιση") || command.get(0).toLowerCase().contains("ανάκληση")) {
                        if (on) {
                            if (command.get(0).toLowerCase().contains("ολική")) {
                                if (swingChoice != 0) {
                                    sentenceToSay = "Λειτουργία ολικής ανάκλισης ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    swingChoice = 0;
                                } else {
                                    sentenceToSay = "Η λειτουργία ολικής ανάκλισης είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("κάτω")) {
                                if (swingChoice != 1) {
                                    sentenceToSay = "Λειτουργία κάτω ανάκλισης ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    swingChoice = 1;
                                } else {
                                    sentenceToSay = "Η λειτουργία κάτω ανάκλισης είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("μέση")) {
                                if (swingChoice != 2) {
                                    sentenceToSay = "Λειτουργία μέσης ανάκλισης ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    swingChoice = 2;
                                } else {
                                    sentenceToSay = "Η λειτουργία μέσης ανάκλισης είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("πάνω")) {
                                if (swingChoice != 3) {
                                    sentenceToSay = "Λειτουργία πάνω ανάκλισης ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    swingChoice = 3;
                                } else {
                                    sentenceToSay = "Η λειτουργία πάνω ανάκλισης είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            }
                        }
                    } else if (command.get(0).toLowerCase().contains("ταχύτητα")) {
                        if (on) {
                            if (command.get(0).toLowerCase().contains("αυτόματη")) {
                                if (fanChoice != 0) {
                                    sentenceToSay = "Λειτουργία ανεμιστήρα σε αυτόματη ταχύτητα ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    fanChoice = 0;
                                } else {
                                    sentenceToSay = "Η λειτουργία ανεμιστήρα σε αυτόματη ταχύτητα είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("χαμηλή")) {
                                if (fanChoice != 1) {
                                    sentenceToSay = "Λειτουργία ανεμιστήρα σε χαμηλή ταχύτητα ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    fanChoice = 1;
                                } else {
                                    sentenceToSay = "Η λειτουργία ανεμιστήρα σε χαμηλή ταχύτητα είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("μεσαία")) {
                                if (fanChoice != 2) {
                                    sentenceToSay = "Λειτουργία ανεμιστήρα σε μεσαία ταχύτητα ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    fanChoice = 2;
                                } else {
                                    sentenceToSay = "Η λειτουργία ανεμιστήρα σε μεσαία ταχύτητα είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            } else if (command.get(0).toLowerCase().contains("υψηλή")) {
                                if (fanChoice != 3) {
                                    sentenceToSay = "Λειτουργία ανεμιστήρα σε υψηλή ταχύτητα ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                    fanChoice = 3;
                                } else {
                                    sentenceToSay = "Η λειτουργία ανεμιστήρα σε υψηλή ταχύτητα είναι ήδη ενεργή.";
                                    TTS.speak(sentenceToSay, TextToSpeech.QUEUE_ADD, null);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gesture.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeft() {
        finish();
        Intent myIntent = new Intent(BlindModeActivity.this, GiantModeActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(myIntent);
    }

    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "el_GR");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInit(int status) {
    }

    // Private class for gestures
    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    BlindModeActivity.this.onLeft();
                }
            } catch (Exception e) {
                Log.e("", "Error on gestures");
            }
            return false;
        }
    }
}
