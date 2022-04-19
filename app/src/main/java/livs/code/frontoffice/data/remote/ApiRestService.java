package livs.code.frontoffice.data.remote;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiRestService {

    //public static final String BASE_URL = "http://10.0.2.2/"; //emulator localhost
    //public static final String BASE_URL = "http://192.168.1.234:3000";//kantor
    //public static final String BASE_URL = "http://192.168.1.80:3000";//ainul-pc
    //public static final String BASE_URL = "http://192.168.1.246:3000";//ainul

    private static Retrofit retrofit =null;
    private static String BASE_URL;

    public static Retrofit getClient(String baseURL) {
        if (retrofit == null) {
        /* Gson gson = new GsonBuilder()
              .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
              .create();

            GsonBuilder builder = new GsonBuilder();
            builder
                    .registerTypeAdapter(Boolean.class, new BooleanTypeAdapter())
                    .registerTypeAdapter(boolean.class, new BooleanTypeAdapter());
            Gson gson = builder.create();

            GsonBuilder builder = new GsonBuilder();
            builder
                    .registerTypeAdapter(ResponseListFormA.class, new ResponseListFormTypeAdapter());
            Gson gson = builder.create();*/
            BASE_URL = baseURL+ "/";
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();

        }
        return retrofit;
    }

    public static void initBaseUrl(String baseUrl) {
        BASE_URL = baseUrl+ "/";
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}