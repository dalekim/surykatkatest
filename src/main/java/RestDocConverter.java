import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author mdaleki
 */
public class RestDocConverter {

    public static void main(final String[] args) throws Exception {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpPost postRequest = new HttpPost("http://192.168.1.104/rest/doc/convert");
        try {
			String s = FileCopyUtils.copyToString(new FileReader("testpdf.txt"));
			System.out.println(s);
			postRequest.setEntity(new StringEntity(s));
			postRequest.addHeader(HttpHeaders.CONTENT_TYPE, "application/xml");
			postRequest.addHeader(HttpHeaders.ACCEPT, "application/pdf");
//			postRequest.addHeader(HttpHeaders.ACCEPT, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			postRequest.setHeader("Authorization", "Basic bWlyYnVzZXI6c3VyeWthdGthMTMo");
			postRequest.getEntity();
            final HttpResponse result = client.execute(postRequest);
			ByteArrayOutputStream boc = new ByteArrayOutputStream();
			result.getEntity().writeTo(boc);

            final FileOutputStream out = new FileOutputStream("testdoc_" + new Date().getTime() + "-1p.pdf");
            StreamUtils.copy(decodeToInputStream(new String(boc.toByteArray())), out);
            out.flush();
            out.close();
        } catch (final ClientProtocolException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

	public static InputStream decodeToInputStream(final String data) {
		if (data != null) {
			final byte[] decodedData = Base64.decodeBase64(data.getBytes());
			return new ByteArrayInputStream(decodedData);
		} else {
			return new ByteArrayInputStream(new byte[0]);
		}
	}


	private static String getString() {
		return "<fillTemplateRequest>\n" +
				"   \n" +
				"        <properties>\n" +
				"            <key>Case.Contact_title__c</key>\n" +
				"            <value>1</value>\n" +
				"        </properties>\n" +
				" <properties>\n" +
				"            <key>Case.Brand__c</key>\n" +
				"            <value>2</value>\n" +
				"        </properties>\n" +
				" <properties>\n" +
				"            <key>Case.Contact_address__c</key>\n" +
				"            <value>3</value>\n" +
				"        </properties>\n" +
				" <properties>\n" +
				"            <key>Case.CaseNumber</key>\n" +
				"            <value>4No</value>\n" +
				"        </properties>\n" +
				"    \n" +
				"</fillTemplateRequest>";
	}

}