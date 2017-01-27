package moe.christina.app.rakonto;

import android.app.Application;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import lombok.experimental.Accessors;

import io.realm.Realm;

import moe.christina.app.rakonto.core.manager.file.AndroidStoryFileManager;
import moe.christina.app.rakonto.di.RakontoApplicationComponentProvider;
import moe.christina.app.rakonto.di.application.DaggerRakontoApplicationComponent;
import moe.christina.app.rakonto.di.application.RakontoApplicationComponent;
import moe.christina.app.rakonto.di.application.module.RakontoApplicationManagerModule;
import moe.christina.app.rakonto.di.application.module.RakontoApplicationRealmModule;

/**
 * TODO:
 * <ul>
 * <li>Animate FAB;</li>
 * <li>Implement two-phased loads;</li>
 * <li>Implement card view loading;</li>
 * <li>Implement stories list position restoring;</li>
 * <li>Hide FAB on scroll;</li>
 * <li>Fix fragment state;</li>
 * <li>Add sorting;</li>
 * <li>Add custom provider permissions;</li>
 * <li>Convert layouts to ConstraintLayout;</li>
 * <li>Implement Leave-behinds;</li>
 * <li>Fix landscape nav bar;</li>
 * <li>Add locale for stories;</li>
 * </ul>
 */
@Accessors(prefix = "_")
public final class RakontoApplication extends Application
    implements RakontoApplicationComponentProvider {
    @Override
    @NonNull
    public final RakontoApplicationComponent getRakontoApplicationComponent() {
        if (_rakontoApplicationComponent == null) {
            throw new IllegalStateException("The application has not yet been created.");
        }

        return _rakontoApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        enableStrictMode(false);

        Realm.init(getApplicationContext());

        _rakontoApplicationComponent = DaggerRakontoApplicationComponent
            .builder()
            .rakontoApplicationRealmModule(new RakontoApplicationRealmModule())
            .rakontoApplicationManagerModule(new RakontoApplicationManagerModule(new AndroidStoryFileManager(
                getFilesDir())))
            .build();

        getRakontoApplicationComponent().getFakeStoryDatabase().create();
    }

    @Nullable
    private RakontoApplicationComponent _rakontoApplicationComponent;

    private void enableStrictMode(final boolean enable) {
        if (enable) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                           .detectAll()
                                           .penaltyLog()
                                           .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                       .detectLeakedSqlLiteObjects()
                                       .detectLeakedClosableObjects()
                                       .penaltyLog()
                                       .penaltyDeath()
                                       .build());
        }
    }
}
