package cl.vasquez.nomadapp.data;

/**
 * Actualizamos la base de datos para incluir la entidad User.
 * Incrementamos la versión a 2 y añadimos fallbackToDestructiveMigration
 * para simplificar durante el desarrollo.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\b"}, d2 = {"Lcl/vasquez/nomadapp/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "postDao", "Lcl/vasquez/nomadapp/data/PostDao;", "userDao", "Lcl/vasquez/nomadapp/data/UserDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {cl.vasquez.nomadapp.data.Post.class, cl.vasquez.nomadapp.data.User.class}, version = 2)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    /**
     * Patrón singleton asegura que la base de datos se cree una sola
     * vez y esté accesible desde toda la app.
     */
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile cl.vasquez.nomadapp.data.AppDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final cl.vasquez.nomadapp.data.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.vasquez.nomadapp.data.PostDao postDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.vasquez.nomadapp.data.UserDao userDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcl/vasquez/nomadapp/data/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcl/vasquez/nomadapp/data/AppDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final cl.vasquez.nomadapp.data.AppDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}