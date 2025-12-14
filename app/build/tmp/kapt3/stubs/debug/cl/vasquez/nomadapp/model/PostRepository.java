package cl.vasquez.nomadapp.model;

/**
 * Este repositorio separa la capa de datos de la vista,
 * cumpliendo el principio de modularidad (MVVM)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\b0\u000bH\u0086@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\r\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0016\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcl/vasquez/nomadapp/model/PostRepository;", "", "postDao", "Lcl/vasquez/nomadapp/data/PostDao;", "(Lcl/vasquez/nomadapp/data/PostDao;)V", "delete", "", "post", "Lcl/vasquez/nomadapp/data/Post;", "(Lcl/vasquez/nomadapp/data/Post;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPostById", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "update", "app_debug"})
public final class PostRepository {
    @org.jetbrains.annotations.NotNull()
    private final cl.vasquez.nomadapp.data.PostDao postDao = null;
    
    public PostRepository(@org.jetbrains.annotations.NotNull()
    cl.vasquez.nomadapp.data.PostDao postDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    cl.vasquez.nomadapp.data.Post post, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<cl.vasquez.nomadapp.data.Post>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    cl.vasquez.nomadapp.data.Post post, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getPostById(int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super cl.vasquez.nomadapp.data.Post> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object update(@org.jetbrains.annotations.NotNull()
    cl.vasquez.nomadapp.data.Post post, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}