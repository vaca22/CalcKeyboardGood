package com.hp.primecalculator.fileprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.net.URLConnection;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import java.net.URLConnection;

/* loaded from: classes.dex */
public abstract class MyContentProvider extends ContentProvider {

    /* renamed from: a  reason: collision with root package name */
    public static final String[] f383a = {"_display_name", "_size"};

    public abstract long a(Uri uri);

    public String b(Uri uri) {
        return uri.getLastPathSegment();
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        throw new RuntimeException("Operation not supported");
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return URLConnection.guessContentTypeFromName(uri.toString());
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new RuntimeException("Operation not supported");
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (strArr == null) {
            strArr = f383a;
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr, 1);
        MatrixCursor.RowBuilder newRow = matrixCursor.newRow();
        for (String str3 : strArr) {
            newRow.add("_display_name".equals(str3) ? b(uri) : "_size".equals(str3) ? Long.valueOf(a(uri)) : null);
        }
        return matrixCursor;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new RuntimeException("Operation not supported");
    }
}
