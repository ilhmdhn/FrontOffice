package livs.code.frontoffice.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.zxing.common.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import livs.code.frontoffice.data.entity.Room;

public final class AppUtils {


    public static final String DATE_TIME_FORMAT_12_HOUR = "MMM d, yyyy  h:mm a";
    public static final String DATE_TIME_FORMAT_24_HOUR = "MMM d, yyyy  k:mm";
    //public static final SimpleDateFormat tanggalJamFrmt = new SimpleDateFormat("dd-MM-yyyy h:mm:ss a");
    //public static final SimpleDateFormat tanggalJamFrmt = new SimpleDateFormat("dd-MM-yyyy kk:mm");
    public static final SimpleDateFormat tanggalJamFrmt = new SimpleDateFormat("dd/MM/yyyy kk:mm");
    public static final SimpleDateFormat tanggalFormatNamaHari = new SimpleDateFormat("EEEE");


    public static final String formatDate(String formatString, Date dateToFormat) {

/*        String userDate = formatDate("d MMM, yyyy", mUserReminderDate);
        formatDate("k:mm", mUserReminderDate);
        formatDate("d MMM, yyyy", date);
        formatDate("h:mm", date);
        amPmString = formatDate("a", date);
        dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
        return simpleDateFormat.format(dateToFormat);
    }

    public static Date strToDate(String mytime) {
        //String mytime="Jan 17, 2012";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date myDate = null;
        try {
            myDate = dateFormat.parse(mytime);
            return myDate;
        } catch (ParseException e) {

            e.printStackTrace();
            return new Date();
        }

       /* SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String finalDate = timeFormat.format(myDate);*/


    }


    public static final String getTanggal(Date dt) {
        //tanggalJamFrmt.setTimeZone(TimeZone.getTimeZone("UTC+07"));
        tanggalJamFrmt.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        return tanggalJamFrmt.format(dt);
    }

    public static final String getTanggalLokal(Date dt) {
        tanggalJamFrmt.setTimeZone(TimeZone.getTimeZone("GMT+07"));
        return tanggalJamFrmt.format(dt);
    }


    public static final String getDay(Date dt) {
        return getDayName(tanggalFormatNamaHari.format(dt));
    }

    public static final String getDayName(String dayInEnglish) {
        String namaHari = null;

        switch (dayInEnglish) {
            case "Monday":
                namaHari = "Senin";
                break;
            case "Tuesday":
                namaHari = "Selasa";
                break;
            case "Wednesday":
                namaHari = "Rabu";
                break;
            case "Thursday":
                namaHari = "Kamis";
                break;
            case "Friday":
                namaHari = "Jumat";
                break;
            case "Saturday":
                namaHari = "Sabtu";
                break;
            case "Sunday":
                namaHari = "Minggu";
                break;
            default:
                System.out.println("Weekend");
                break;
        }
        return namaHari;
    }

    private static final String getTanggalR(Date dt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy kk:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));

        return simpleDateFormat.format(dt);
    }

    public static String formatNominal(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        formatRupiah.getCurrency().getSymbol(localeID);



        formatRupiah.setMaximumFractionDigits(0);//hilangkan 2 koma paling belakang
        String nominal = formatRupiah.format(number).replaceAll("Rp","");//hilangkan RP
        return nominal;
    }

    public static String formatRupiah(Double number){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        formatRupiah.getCurrency().getSymbol(localeID);



        formatRupiah.setMaximumFractionDigits(0);//hilangkan 2 koma paling belakang
        return formatRupiah.format(number).replaceAll("Rp","");
    }

    public static String getFirstWord(String myString){
        String arr[] = myString.split(" ");
        String firstWord = arr[0];

        return firstWord;
    }
    public static String timeReminder(Date startDate, Date endDate) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy kk:mm");
        Date currentTime = Calendar.getInstance().getTime();
        String bk = " * ";
        try {
            Date endD = simpleDateFormat.parse(getTanggalR(endDate));
            if (Math.abs(currentTime.getTime()) > Math.abs(endD.getTime())) {
                bk = " | END";
                return bk;
            }
            long different = Math.abs(endD.getTime() - currentTime.getTime());



            System.out.println("currentDate : " + currentTime);
            System.out.println("endDate : " + endD);
            System.out.println("different : " + different);


          /*  long seconds = different / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;*/

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedHours > 0) {
                bk = " | SISA " + elapsedHours + " JAM " + elapsedMinutes + " MENIT";
            } else {
                bk = " | SISA " + elapsedMinutes + " MENIT";
            }


            return bk;

        } catch (ParseException e) {
            e.printStackTrace();
            return "*";
        }






       /* System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/


    }

    public static long timeReminder1(Date startDate, Date endDate) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy kk:mm");
        Date currentTime = Calendar.getInstance().getTime();
        long bk = 0;
        try {
            Date endD = simpleDateFormat.parse(getTanggalR(endDate));
            if (Math.abs(currentTime.getTime()) > Math.abs(endD.getTime())) {
                //bk = " | END";
                bk=0;
                return bk;
            }
            long different = Math.abs(endD.getTime() - currentTime.getTime());



            System.out.println("currentDate : " + currentTime);
            System.out.println("endDate : " + endD);
            System.out.println("different : " + different);


          /*  long seconds = different / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;*/

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            if (elapsedHours > 0) {
                bk = (elapsedHours*60)+elapsedMinutes;
            } else {
                bk = elapsedMinutes;
            }

            return bk;

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }






       /* System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/


    }

    public static boolean strToBoolean(String code) {
        boolean result = false;
        if (code.equals("0") || code.equals("false")) {
            result = false;
        } else if (code.equals("1") || code.equals("true")) {
            result = true;
        }
        return result;

       /* return code == "0" || code == "false" ? false :
                code == "1" || code =="true"? true :
                        null;*/
    }

    public static String jsonElementToString(JsonElement data) {
        return data.toString().equals("null") ? "-" : data.getAsString();
    }

    public static Double jsonElementToDouble(JsonElement data) {
        return data.toString().equals("null") ? 0.1 : data.getAsDouble();
    }

    public static Long jsonElementToLong(JsonElement data) {
        return data.toString().equals("null") ? 1 : data.getAsLong();
    }

    public static Integer jsonElementToInt(JsonElement data) {
        return data.toString().equals("null") ? 1 : data.getAsInt();
    }

    public static Date getCurrentDateTime(){
        Date currentDate =  Calendar.getInstance().getTime();
        return currentDate;
    }

    public static String getFormattedDateString(Date date) {

        try {

            SimpleDateFormat spf = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            String dateString = spf.format(date);

            Date newDate = spf.parse(dateString);
            spf= new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            return spf.format(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateHash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            byte byteData[] = md.digest();
            String base64 = Base64.encodeToString(byteData, Base64.NO_WRAP);
            return base64;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void openKeyboard(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        }, 500);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String formatSisaWaktuCheckin(Room room) {
        String kata = "";
        if (room.getRoomResidualCheckinHoursTime() < 1 &&
                room.getRoomResidualCheckinHoursMinutesTime() <1) {
            return "Waktu Habis";
        } else {
            kata = "Sisa ";
            if (room.getRoomResidualCheckinHoursTime() > 0) {
                kata = kata + room.getRoomResidualCheckinHoursTime() + " jam ";
            }

            kata = kata + room.getRoomResidualCheckinHoursMinutesTime() + " menit ";
        }

        return kata;
    }
}