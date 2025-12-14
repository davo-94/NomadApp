package cl.vasquez.nomadapp.viewmodel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J4\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00052\u0006\u0010\u0011\u001a\u00020\u00122\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u0014J\u001e\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00062\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u0014J4\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u00062\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00052\u0006\u0010\u0011\u001a\u00020\u00122\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u0014J\u001e\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\u000e2\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u0014J\u0006\u0010\u001a\u001a\u00020\fJ&\u0010\u001b\u001a\u00020\f2\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u00062\u000e\b\u0002\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u0014R\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u001c"}, d2 = {"Lcl/vasquez/nomadapp/viewmodel/PostViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_posts", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcl/vasquez/nomadapp/data/remote/dto/PostDto;", "posts", "Lkotlinx/coroutines/flow/StateFlow;", "getPosts", "()Lkotlinx/coroutines/flow/StateFlow;", "addImagesToPost", "", "postId", "", "imageUris", "Landroid/net/Uri;", "context", "Landroid/content/Context;", "onSuccess", "Lkotlin/Function0;", "createPost", "post", "createPostWithImages", "deletePost", "id", "loadPosts", "updatePost", "app_debug"})
public final class PostViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<cl.vasquez.nomadapp.data.remote.dto.PostDto>> _posts = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<cl.vasquez.nomadapp.data.remote.dto.PostDto>> posts = null;
    
    public PostViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<cl.vasquez.nomadapp.data.remote.dto.PostDto>> getPosts() {
        return null;
    }
    
    public final void createPost(@org.jetbrains.annotations.NotNull()
    cl.vasquez.nomadapp.data.remote.dto.PostDto post, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void createPostWithImages(@org.jetbrains.annotations.NotNull()
    cl.vasquez.nomadapp.data.remote.dto.PostDto post, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends android.net.Uri> imageUris, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void addImagesToPost(long postId, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends android.net.Uri> imageUris, @org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void loadPosts() {
    }
    
    public final void updatePost(long id, @org.jetbrains.annotations.NotNull()
    cl.vasquez.nomadapp.data.remote.dto.PostDto post, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
    
    public final void deletePost(long id, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSuccess) {
    }
}