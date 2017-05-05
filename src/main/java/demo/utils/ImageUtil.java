package demo.utils;
/**
 * @author zhanglm E-mail:zhanglm@hdxt.net.cn
 * @version 创建时间：2008-3-21 下午02:59:36
 * 类说明
 */

public class ImageUtil {
	
//	private Font mFont = new Font("Arial Black", Font.PLAIN, 16);
//	private sun.misc.BASE64Encoder encode = new sun.misc.BASE64Encoder();
//	
//	Color getRandColor(int fc, int bc) {
//		Random random = new Random();
//		if (fc > 255)
//			fc = 255;
//		if (bc > 255)
//			bc = 255;
//		int r = fc + random.nextInt(bc - fc);
//		int g = fc + random.nextInt(bc - fc);
//		int b = fc + random.nextInt(bc - fc);
//		return new Color(r, g, b);
//	}
//
//	private String getRandomChar() {
//		int rand = (int) Math.round(Math.random() * 2);
//		long itmp = 0;
//		char ctmp = '\u0000';
//		switch (rand) {
//		case 1:
//			itmp = Math.round(Math.random() * 25 + 65);
//			ctmp = (char) itmp;
//			return String.valueOf(ctmp);
//		case 2:
//			itmp = Math.round(Math.random() * 25 + 97);
//			ctmp = (char) itmp;
//			return String.valueOf(ctmp);
//		default:
//			itmp = Math.round(Math.random() * 9);
//			return String.valueOf(itmp);
//		}
//	}
//	/**
//	 * 生成图片编码
//	 * @return map(key,value)
//	 */
//	public  Map produce() {
////		//图片文字内容
////		String sRand = ""; 
////		//图片base64编码字符串
////		String base64str = "";
////		
////		int width = 100, height = 18;
////		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
////
////		Graphics g = image.getGraphics();
////		Random random = new Random();
////		g.setColor(getRandColor(200, 250));
////		g.fillRect(1, 1, width - 1, height - 1);
////		g.setColor(new Color(102, 102, 102));
////		g.drawRect(0, 0, width - 1, height - 1);
////		g.setFont(mFont);
////
////		g.setColor(getRandColor(160, 200));
////		for (int i = 0; i < 155; i++) {
////			int x = random.nextInt(width - 1);
////			int y = random.nextInt(height - 1);
////			int xl = random.nextInt(6) + 1;
////			int yl = random.nextInt(12) + 1;
////			g.drawLine(x, y, x + xl, y + yl);
////		}
////		for (int i = 0; i < 70; i++) {
////			int x = random.nextInt(width - 1);
////			int y = random.nextInt(height - 1);
////			int xl = random.nextInt(12) + 1;
////			int yl = random.nextInt(6) + 1;
////			g.drawLine(x, y, x - xl, y - yl);
////		}
////
////		for (int i = 0; i < 6; i++) {
////			String tmp = getRandomChar();
////			sRand += tmp;
////			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
////			g.drawString(tmp, 15 * i + 10, 15);
////		}
////		g.dispose();
////		ByteArrayOutputStream bs = new ByteArrayOutputStream();		
////		int n = 0;
////		try {
////			ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
////			ImageIO.write(image, "jpg", imOut);
////			InputStream is = new ByteArrayInputStream(bs.toByteArray());			
////			while ((n = is.available()) > 0) {
////				byte[] b = new byte[n];
////				int result = is.read(b);
////				if (result == -1)
////					break;
////				//得到base64编码串   
////				base64str += encode.encode(b); 
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////		}		
////		HashMap<String, String> map = new HashMap<String, String>();
////		map.put("key", sRand);
////		map.put("value", base64str);
////		return map;
//	}
	
	public void write(String fromString) {
//
//		try {
//			//还原后的图片名
//			FileOutputStream fo = new FileOutputStream("e:/get1.jpg"); 
//			//base64解码类
//			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder(); 
//			byte[] b = decoder.decodeBuffer(fromString);
//			//写入图片文件
//			fo.write(b); 
//			fo.close();
//		} catch (Exception e) {
//			System.out.println("write method error is " + e.toString());
//		}
	}

}
