package com.gstar_info.lab.com.checkinclass.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 位图工具类
 * 
 * @author jst
 */
public class BitmapUtil {

	/**
	 * 图片等比例压缩
	 *
	 * @param filePath
	 * @param reqWidth 期望的宽
	 * @param reqHeight 期望的高
	 * @return
	 */
	public static Bitmap decodeSampledBitmap(String filePath, int reqWidth,
											 int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算InSampleSize
	 * 宽的压缩比和高的压缩比的较小值  取接近的2的次幂的值
	 * 比如宽的压缩比是3 高的压缩比是5 取较小值3  而InSampleSize必须是2的次幂，取接近的2的次幂4
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
											int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			int ratio = heightRatio < widthRatio ? heightRatio : widthRatio;
			// inSampleSize只能是2的次幂  将ratio就近取2的次幂的值
			if (ratio < 3)
				inSampleSize =  ratio;
			else if (ratio < 6.5)
				inSampleSize = 4;
			else if (ratio < 8)
				inSampleSize = 8;
			else
				inSampleSize =  ratio;
		}

		return inSampleSize;
	}

	/**
	 * 图片缩放到指定宽高
	 * 
	 * 非等比例压缩，图片会被拉伸
	 * 
	 * @param bitmap 源位图对象
	 * @param w 要缩放的宽度
	 * @param h 要缩放的高度
	 * @return 新Bitmap对象
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,matrix, false);
		return newBmp;
	}

	public static Bitmap getBitmapFromUri(Uri uri)
	{
		try
		{
			// 读取uri所在的图片
			Bitmap bitmap = MediaStore.Images.Media.getBitmap(MyApplication.getGlobalContext().getContentResolver(), uri);
			return bitmap;
		}
		catch (Exception e)
		{
			Log.e("[Android]", e.getMessage());
			Log.e("[Android]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}

	public static String compressImage(Bitmap bitmap, String outPath,int quality) throws FileNotFoundException {
		FileOutputStream os = new FileOutputStream(outPath);
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
		return outPath;
	}

	public static String compressAndGenImage(Bitmap image, String outPath) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// scale
		int options = 100;
		// Store the bitmap into output stream(no compress)
		image.compress(Bitmap.CompressFormat.JPEG, options, os);
		// Compress by loop
		while ( os.toByteArray().length / 1024 > 1024) {
			// Clean up os
			os.reset();
			// interval 10
			options -= 10;
			image.compress(Bitmap.CompressFormat.JPEG, options, os);
		}

		// Generate compressed image file
		FileOutputStream fos = new FileOutputStream(outPath);
		fos.write(os.toByteArray());
		fos.flush();
		fos.close();

		return outPath;
	}

	/**
	 * 获取网络图片
	 * @param imageurl 图片网络地址
	 * @return Bitmap 返回位图
	 */
	public static Bitmap GetImageInputStream(String imageurl){
		URL url;
		HttpURLConnection connection=null;
		Bitmap bitmap=null;
        InputStream inputStream = null;
		try {
			url = new URL(imageurl);
			connection=(HttpURLConnection)url.openConnection();
			connection.setConnectTimeout(6000); //超时设置
			connection.setDoInput(true);
            connection.connect();
            inputStream=connection.getInputStream();
			bitmap=BitmapFactory.decodeStream(inputStream);
            inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return bitmap;
	}


	/**
	 * 保存位图到本地
	 * @param bitmap
	 * @param path 本地路径
	 * @return void
	 */
	public static void SavaImage(Bitmap bitmap, String path){
		File file=new File(path);
		FileOutputStream fileOutputStream=null;
		//文件夹不存在，则创建它
		if(!file.exists()){
			file.mkdir();
		}
		try {
			fileOutputStream=new FileOutputStream(path+"/"+System.currentTimeMillis()+".png");
            Log.d("imageutil_url",path+"/"+System.currentTimeMillis()+".png");
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
