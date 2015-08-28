package pl.pola_app.internal.di;


import com.google.gson.Gson;
import com.octo.android.robospice.SpiceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.pola_app.network.PolaSpiceService;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Converter provideConverter(Gson gson) {
        return new GsonConverter(gson);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    SpiceManager provideSpiceManager() {
        return new SpiceManager(PolaSpiceService.class);
    }
}
