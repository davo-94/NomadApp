package cl.vasquez.nomadapp.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&\u00a8\u0006\f"}, d2 = {"Lcl/vasquez/nomadapp/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "adminUserDao", "Lcl/vasquez/nomadapp/data/AdminUserDao;", "contactDao", "Lcl/vasquez/nomadapp/data/ContactDao;", "postDao", "Lcl/vasquez/nomadapp/data/PostDao;", "userDao", "Lcl/vasquez/nomadapp/data/UserDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {cl.vasquez.nomadapp.data.Post.class, cl.vasquez.nomadapp.data.Contact.class, cl.vasquez.nomadapp.data.User.class, cl.vasquez.nomadapp.data.AdminUser.class}, version = 6, exportSchema = true)
@androidx.room.TypeConverters(value = {cl.vasquez.nomadapp.data.Converters.class})
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    /**
     * Patrón singleton asegura que la base de datos se cree una sola
     * vez y esté accesible desde toda la app.
     */
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile cl.vasquez.nomadapp.data.AppDatabase INSTANCE;
    
    /**
     * Migración desde versión 3 → 4.
     * Agrega la nueva columna imageUris (TEXT) en la tabla de publicaciones.
     * Si existía imageUri, se migra su valor al nuevo campo.
     * (Desactivada temporalmente mientras se usa fallbackToDestructiveMigration)
     */
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_3_4 = null;
    @org.jetbrains.annotations.NotNull()
    public static final cl.vasquez.nomadapp.data.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.vasquez.nomadapp.data.PostDao postDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.vasquez.nomadapp.data.ContactDao contactDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.vasquez.nomadapp.data.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract cl.vasquez.nomadapp.data.AdminUserDao adminUserDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcl/vasquez/nomadapp/data/AppDatabase$Companion;", "", "()V", "INSTANCE", "Lcl/vasquez/nomadapp/data/AppDatabase;", "MIGRATION_3_4", "Landroidx/room/migration/Migration;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Este bloque garantiza una sola instancia global y evita que se creen varias bases
         * al mismo tiempo en caso. Asegura que todos los componentes usen la misma DB.
         */
        @org.jetbrains.annotations.NotNull()
        public final cl.vasquez.nomadapp.data.AppDatabase getDatabase(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}