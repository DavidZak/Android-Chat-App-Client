package com.example.mradmin.androidnavapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import com.example.mradmin.androidnavapp.Activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mrAdmin on 04.10.2017.
 */


public class FoneService extends Service {

    // ÈÌß ÑÅÐÂÅÐÀ (url çàðåãèñòðèðîâàííîãî íàìè ñàéòà)
    // íàïðèìåð http://l29340eb.bget.ru
    String server_name = "http://l29340eb.bget.ru";

    //SQLiteDatabase chatDBlocal;
    HttpURLConnection conn;
    Cursor cursor;
    Thread thr;
    ContentValues new_mess;
    Long last_time; // âðåìÿ ïîñëåäíåé çàïèñè â ÁÄ, îòñåêàåì ïî íåìó ÷òî íàì
    // òÿíóòü ñ ñåðâåðà, à ÷òî óæå åñòü

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onStart(Intent intent, int startId) {

        Log.i("chat", "+ FoneService - çàïóñê ñåðâèñà");

        //chatDBlocal = openOrCreateDatabase("chatDBlocal.db",
        //        Context.MODE_PRIVATE, null);
        //chatDBlocal
        //        .execSQL("CREATE TABLE IF NOT EXISTS chat (_id integer primary key autoincrement, author, client, data, text)");

        // ñîçäàäèì è ïîêàæåì notification
        // ýòî ïîçâîëèò ñòàòü ñåðâèñó "áåññìåðòíûì"
        // è áóäåò âèçóàëüíî âèäíî â òðåå

        System.out.println("=========================================================================== SERVICE STARTED");

        startLoop();
    }

    // çàïóñê ïîòîêà, âíóòðè êîòîðîãî áóäåò ïðîèñõîäèòü
    // ðåãóëÿðíîå ñîåäèíåíèå ñ ñåðâåðîì äëÿ ÷òåíèÿ íîâûõ
    // ñîîáùåíèé.
    // åñëè ñîîáùåíèÿ íàéäåíû - îòïðàâèì áðîàäêàñò äëÿ îáíîâëåíèÿ
    // ListView â ChatActivity
    private void startLoop() {

        thr = new Thread(new Runnable() {

            // answer = îòâåò íà çàïðîñ
            // lnk = ëèíê ñ ïàðàìåòðàìè
            String answer, lnk;

            public void run() {

                while (true) { // ñòàðòóåì áåñêîíå÷íûé öèêë

                    // ãëÿíåì ëîêàëüíóþ ÁÄ íà íàëè÷èå ñîîáùùåíèé ÷àòà
                    //cursor = chatDBlocal.rawQuery(
                    //        "SELECT * FROM chat ORDER BY data", null);

                    // åñëè êàêèå-ëèáî ñîîáùåíèÿ åñòü - ôîðìèðóåì çàïðîñ
                    // ïî êîòîðîìó ïîëó÷èì òîëüêî íîâûå ñîîáùåíèÿ

                    /*if (cursor.moveToLast()) {
                        last_time = cursor.getLong(cursor
                                .getColumnIndex("data"));
                        lnk = server_name + "/chat.php?action=select&data="
                                + last_time.toString();

                        // åñëè ñîîáùåíèé â ÁÄ íåò - ôîðìèðóåì çàïðîñ
                        // ïî êîòîðîìó ïîëó÷èì âñ¸
                    } else {
                        lnk = server_name + "/chat.php?action=select";
                    }

                    cursor.close();

                    // ñîçäàåì ñîåäèíåíèå ---------------------------------->
                    try {
                        Log.i("chat",
                                "+ FoneService --------------- ÎÒÊÐÎÅÌ ÑÎÅÄÈÍÅÍÈÅ");

                        conn = (HttpURLConnection) new URL(lnk)
                                .openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                        conn.setDoInput(true);
                        conn.connect();

                    } catch (Exception e) {
                        Log.i("chat", "+ FoneService îøèáêà: " + e.getMessage());
                    }
                    // ïîëó÷àåì îòâåò ---------------------------------->
                    try {
                        InputStream is = conn.getInputStream();
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(is, "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String bfr_st = null;
                        while ((bfr_st = br.readLine()) != null) {
                            sb.append(bfr_st);
                        }

                        Log.i("chat", "+ FoneService - ïîëíûé îòâåò ñåðâåðà:\n"
                                + sb.toString());
                        // ñôîðìèðóåì îòâåò ñåðâåðà â string
                        // îáðåæåì â ïîëó÷åííîì îòâåòå âñå, ÷òî íàõîäèòñÿ çà "]"
                        // ýòî íåîáõîäèìî, ò.ê. json îòâåò ïðèõîäèò ñ ìóñîðîì
                        // è åñëè ýòîò ìóñîð íå óáðàòü - áóäåò íåâàëèäíûì
                        answer = sb.toString();
                        answer = answer.substring(0, answer.indexOf("]") + 1);

                        is.close(); // çàêðîåì ïîòîê
                        br.close(); // çàêðîåì áóôåð

                    } catch (Exception e) {
                        Log.i("chat", "+ FoneService îøèáêà: " + e.getMessage());
                    } finally {
                        conn.disconnect();
                        Log.i("chat",
                                "+ FoneService --------------- ÇÀÊÐÎÅÌ ÑÎÅÄÈÍÅÍÈÅ");
                    }

                    // çàïèøåì îòâåò â ÁÄ ---------------------------------->
                    if (answer != null && !answer.trim().equals("")) {

                        Log.i("chat",
                                "+ FoneService ---------- îòâåò ñîäåðæèò JSON:");

                        try {
                            // îòâåò ïðåâðàòèì â JSON ìàññèâ
                            JSONArray ja = new JSONArray(answer);
                            JSONObject jo;

                            Integer i = 0;

                            while (i < ja.length()) {

                                // ðàçáåðåì JSON ìàññèâ ïîñòðî÷íî
                                jo = ja.getJSONObject(i);

                                Log.i("chat",
                                        "=================>>> "
                                                + jo.getString("author")
                                                + " | "
                                                + jo.getString("client")
                                                + " | " + jo.getLong("data")
                                                + " | " + jo.getString("text"));

                                // ñîçäàäèì íîâîå ñîîáùåíèå
                                new_mess = new ContentValues();
                                new_mess.put("author", jo.getString("author"));
                                new_mess.put("client", jo.getString("client"));
                                new_mess.put("data", jo.getLong("data"));
                                new_mess.put("text", jo.getString("text"));
                                // çàïèøåì íîâîå ñîîáùåíèå â ÁÄ
                                //chatDBlocal.insert("chat", null, new_mess);
                                new_mess.clear();

                                i++;
*/
                                // îòïðàâèì áðîàäêàñò äëÿ ChatActivity
                                // åñëè îíà îòêðûòà - îíà îáíîâèòü ListView
                                sendBroadcast(new Intent(
                                        "com.example.action.UPDATE_RecyclerView"));
                       /*     }
                        } catch (Exception e) {
                            // åñëè îòâåò ñåðâåðà íå ñîäåðæèò âàëèäíûé JSON
                            Log.i("chat",
                                    "+ FoneService ---------- îøèáêà îòâåòà ñåðâåðà:\n"
                                            + e.getMessage());*/
                        //}
                    //} else {
                        // åñëè îòâåò ñåðâåðà ïóñòîé
                    //    Log.i("chat",
                    //            "+ FoneService ---------- îòâåò íå ñîäåðæèò JSON!");
                    //}

                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                        Log.i("chat",
                                "+ FoneService - îøèáêà ïðîöåññà: "
                                        + e.getMessage());
                    }
                }
            }
        });

        thr.setDaemon(true);
        thr.start();

    }
}
