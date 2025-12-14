package cl.vasquez.nomadapp.utils;

/**
 * Object (singleton) que agrupa todas las funciones de validación para formularios utilizados.
 * Adaptado del modelo TiendApp (semana 8)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0004J\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0007\u001a\u00020\u0004J\u0010\u0010\b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\t\u001a\u00020\u0004J\u0010\u0010\n\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u0005\u001a\u00020\u0004J\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\u0004J\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0004J\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000b\u001a\u00020\u0004\u00a8\u0006\u0011"}, d2 = {"Lcl/vasquez/nomadapp/utils/ValidationUtils;", "", "()V", "getEmailErrorMessage", "", "email", "getMensajeErrorMessage", "mensaje", "getNombreErrorMessage", "nombre", "getPasswordErrorMessage", "password", "isValidEmail", "", "isValidMensaje", "isValidNombre", "isValidPassword", "app_debug"})
public final class ValidationUtils {
    @org.jetbrains.annotations.NotNull()
    public static final cl.vasquez.nomadapp.utils.ValidationUtils INSTANCE = null;
    
    private ValidationUtils() {
        super();
    }
    
    public final boolean isValidNombre(@org.jetbrains.annotations.NotNull()
    java.lang.String nombre) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getNombreErrorMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String nombre) {
        return null;
    }
    
    public final boolean isValidEmail(@org.jetbrains.annotations.NotNull()
    java.lang.String email) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getEmailErrorMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String email) {
        return null;
    }
    
    public final boolean isValidMensaje(@org.jetbrains.annotations.NotNull()
    java.lang.String mensaje) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMensajeErrorMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String mensaje) {
        return null;
    }
    
    public final boolean isValidPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String password) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPasswordErrorMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String password) {
        return null;
    }
}