package cl.vasquez.nomadapp.data;

/**
 * Object (singleton; solo una instancia) que gestiona la sesión del usuario mediante DataStore.
 * Corregido para evitar errores de inicialización del contexto.
 * Manejo de sesiones global.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u000b\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u000bJ\u000e\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u000bJ\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\tJ\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bJ\u000e\u0010\u0013\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0014J\b\u0010\u0015\u001a\u00020\tH\u0002J(\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u00052\b\b\u0002\u0010\u0019\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u001aR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcl/vasquez/nomadapp/data/SessionManager;", "", "()V", "REMEMBER_SESSION", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "USER_EMAIL", "USER_ROLE", "context", "Landroid/content/Context;", "getUserEmail", "Lkotlinx/coroutines/flow/Flow;", "getUserRole", "hasActiveSession", "", "initialize", "", "appContext", "isSessionRemembered", "logout", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "requireContext", "saveUserSession", "email", "role", "rememberSession", "(Ljava/lang/String;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class SessionManager {
    @org.jetbrains.annotations.Nullable()
    private static android.content.Context context;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> USER_EMAIL = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> USER_ROLE = null;
    @org.jetbrains.annotations.NotNull()
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> REMEMBER_SESSION = null;
    @org.jetbrains.annotations.NotNull()
    public static final cl.vasquez.nomadapp.data.SessionManager INSTANCE = null;
    
    private SessionManager() {
        super();
    }
    
    public final void initialize(@org.jetbrains.annotations.NotNull()
    android.content.Context appContext) {
    }
    
    private final android.content.Context requireContext() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveUserSession(@org.jetbrains.annotations.NotNull()
    java.lang.String email, @org.jetbrains.annotations.NotNull()
    java.lang.String role, boolean rememberSession, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getUserEmail() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getUserRole() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logout(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> hasActiveSession() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isSessionRemembered() {
        return null;
    }
}