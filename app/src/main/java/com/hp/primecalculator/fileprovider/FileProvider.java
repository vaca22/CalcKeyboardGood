package com.hp.primecalculator.fileprovider;

import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.hp.primecalculator.CalcApplication;

import java.io.File;
import java.io.FileNotFoundException;



/* loaded from: classes.dex */
public class FileProvider extends MyContentProvider {

    /* renamed from: b  reason: collision with root package name */
    public static final Uri f433b = Uri.parse("content://com.hp.primecalculator/");

    @Override // c.b.a.d.a
    public long a(Uri uri) {
        return new File(new File(CalcApplication.G + "/docs/"), uri.getPath()).length();
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        int i;
        File file = new File(new File(CalcApplication.G + "/docs/"), uri.getPath());
        if (file.exists()) {
            if ("r".equals(str)) {
                i = 268435456;
            } else if ("w".equals(str) || "wt".equals(str)) {
                i = 738197504;
            } else if ("wa".equals(str)) {
                i = 704643072;
            } else if ("rw".equals(str)) {
                i = 939524096;
            } else if ("rwt".equals(str)) {
                i = 1006632960;
            } else {
                throw new IllegalArgumentException("Bad mode '" + str + "'");
            }
            return ParcelFileDescriptor.open(file, i);
        }
        throw new FileNotFoundException(uri.getPath());
    }
}
