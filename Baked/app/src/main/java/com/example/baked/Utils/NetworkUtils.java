package com.example.baked.Utils;

        import android.content.Context;
        import android.net.ConnectivityManager;
        import android.net.NetworkCapabilities;
        import android.net.NetworkInfo;
        import android.os.Build;
        import android.util.Log;

public class NetworkUtils {

    // Checks if user has an internet connection to be able to load data
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                        return true;
                    }
                }
            }
            else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("UPDATE_STATUS", "Network available");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("UPDATE_STATUS", "" + e.getMessage());
                }
            }
        }
        Log.i("UPDATE_STATUS","Network unavailable.");
        return false;
    }
}
