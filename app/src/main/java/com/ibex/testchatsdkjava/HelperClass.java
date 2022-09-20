package com.ibex.testchatsdkjava;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;



@Metadata(
        mv = {1, 6, 0},
        k = 1,
        d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J9\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\u00042\u000e\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u000b¢\u0006\u0002\u0010\fJ\u0018\u0010\r\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0007\u001a\u00020\b¨\u0006\u0012"},
        d2 = {"Lcom/ibex/testchatsdkjava/URIPathHelper;", "", "()V", "getDataColumn", "", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "selection", "selectionArgs", "", "(Landroid/content/Context;Landroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Ljava/lang/String;", "getPath", "isDownloadsDocument", "", "isExternalStorageDocument", "isMediaDocument", "TestChatSDKJava.app.main"}
)
public class HelperClass {

    public HelperClass() {
    }

    @Nullable
        public final String getPath(@NotNull Context context, @NotNull Uri uri) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(uri, "uri");
            boolean isKitKatorAbove = Build.VERSION.SDK_INT >= 19;
            if (isKitKatorAbove && DocumentsContract.isDocumentUri(context, uri)) {
                String docId;
                String[] split;
                CharSequence var6;
                String var7;
                byte var8;
                Collection $this$toTypedArray$iv;
                String type;
                Regex var13;
                boolean $i$f$toTypedArray;
                Object[] var10000;
                if (this.isExternalStorageDocument(uri)) {
                    docId = DocumentsContract.getDocumentId(uri);
                    Intrinsics.checkNotNullExpressionValue(docId, "docId");
                    var6 = (CharSequence)docId;
                    var7 = ":";
                    var13 = new Regex(var7);
                    var8 = 0;
                    $this$toTypedArray$iv = (Collection)var13.split(var6, var8);
                    $i$f$toTypedArray = false;
                    var10000 = $this$toTypedArray$iv.toArray(new String[0]);
                    Intrinsics.checkNotNull(var10000, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                    split = (String[])var10000;
                    type = split[0];
                    if (StringsKt.equals("primary", type, true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1];
                    }
                } else {
                    if (this.isDownloadsDocument(uri)) {
                        docId = DocumentsContract.getDocumentId(uri);
                        Uri var17 = Uri.parse("content://downloads/public_downloads");
                        Long var10001 = Long.valueOf(docId);
                        Intrinsics.checkNotNullExpressionValue(var10001, "java.lang.Long.valueOf(id)");
                        var17 = ContentUris.withAppendedId(var17, var10001);
                        Intrinsics.checkNotNullExpressionValue(var17, "ContentUris.withAppended…va.lang.Long.valueOf(id))");
                        Uri contentUri = var17;
                        return this.getDataColumn(context, contentUri, (String)null, (String[])null);
                    }

                    if (this.isMediaDocument(uri)) {
                        docId = DocumentsContract.getDocumentId(uri);
                        Intrinsics.checkNotNullExpressionValue(docId, "docId");
                        var6 = (CharSequence)docId;
                        var7 = ":";
                        var13 = new Regex(var7);
                        var8 = 0;
                        $this$toTypedArray$iv = (Collection)var13.split(var6, var8);
                        $i$f$toTypedArray = false;
                        var10000 = $this$toTypedArray$iv.toArray(new String[0]);
                        Intrinsics.checkNotNull(var10000, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
                        split = (String[])var10000;
                        type = split[0];
                        Uri contentUri = (Uri)null;
                        if (Intrinsics.areEqual("image", type)) {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        } else if (Intrinsics.areEqual("video", type)) {
                            contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        } else if (Intrinsics.areEqual("audio", type)) {
                            contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        String selection = "_id=?";
                        String[] selectionArgs = new String[]{split[1]};
                        return this.getDataColumn(context, contentUri, selection, selectionArgs);
                    }
                }
            } else {
                if (StringsKt.equals("content", uri.getScheme(), true)) {
                    return this.getDataColumn(context, uri, (String)null, (String[])null);
                }

                if (StringsKt.equals("file", uri.getScheme(), true)) {
                    return uri.getPath();
                }
            }

            return null;
        }

        @Nullable
        public final String getDataColumn(@NotNull Context context, @Nullable Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
            Intrinsics.checkNotNullParameter(context, "context");
            Cursor cursor = (Cursor)null;
            String column = "_data";
            String[] projection = new String[]{column};
            boolean var11 = false;

            String var9;
            label69: {
                try {
                    var11 = true;
                    cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, (String)null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            int column_index = cursor.getColumnIndexOrThrow(column);
                            var9 = cursor.getString(column_index);
                            var11 = false;
                            break label69;
                        }

                        var11 = false;
                    } else {
                        var11 = false;
                    }
                } finally {
                    if (var11) {
                        if (cursor != null) {
                            cursor.close();
                        }

                    }
                }

                if (cursor != null) {
                    cursor.close();
                }

                return null;
            }

            cursor.close();
            return var9;
        }

        public final boolean isExternalStorageDocument(@NotNull Uri uri) {
            Intrinsics.checkNotNullParameter(uri, "uri");
            return Intrinsics.areEqual("com.android.externalstorage.documents", uri.getAuthority());
        }

        public final boolean isDownloadsDocument(@NotNull Uri uri) {
            Intrinsics.checkNotNullParameter(uri, "uri");
            return Intrinsics.areEqual("com.android.providers.downloads.documents", uri.getAuthority());
        }

        public final boolean isMediaDocument(@NotNull Uri uri) {
            Intrinsics.checkNotNullParameter(uri, "uri");
            return Intrinsics.areEqual("com.android.providers.media.documents", uri.getAuthority());
        }


}
