package com.hnjz.license;

import com.hnjz.common.plugins.IDisposable;
import com.hnjz.common.plugins.Initializable;
import com.hnjz.common.plugins.impl.AbstractConfigurablePlugin;
import com.hnjz.core.plugins.base.ILicenser;
import com.hnjz.license.impl.DefaultFileExtractionAlgorithm;
import com.hnjz.license.impl.PeriodicLicenseFileExtractor;
import com.hnjz.license.impl.common.PeriodicLicenseBean;
import com.hnjz.license.impl.common.PeriodicLicenseValidator;
import com.hnjz.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/17 11:27
 */
public class DefaultLicenser extends AbstractConfigurablePlugin implements ILicenser, Initializable, IDisposable {
    private static Log log = LogFactory.getLog(com.hnjz.license.DefaultLicenser.class);
    private String licenseFile = "/META-INF/lic.dat";
    private PeriodicLicenseBean license = null;

    public DefaultLicenser() {
    }

    protected void doConfig(Map<String, String> configMap) {
        String file = (String)configMap.get("license-file");
        if (StringHelper.isNotAnyEmpty(new String[]{file})) {
            this.licenseFile = file;
        }

    }

    public boolean validate() throws Exception {
        if (this.license == null) {
            return false;
        } else {
            Calendar car = Calendar.getInstance();
            PeriodicLicenseValidator validator = new PeriodicLicenseValidator(car);
            return validator.validate(this.license);
        }
    }

    public void initialize() {
        URL url = PeriodicLicenseFileExtractor.class.getResource(this.licenseFile);
        if (url == null) {
            throw new RuntimeException("License file is missing!");
        } else {
            try {
                String fileName = url.getPath();
                if (fileName != null) {
                    fileName = URLDecoder.decode(fileName, "UTF-8");
                }

                PeriodicLicenseFileExtractor extractor = new PeriodicLicenseFileExtractor(new File(fileName));
                this.license = (PeriodicLicenseBean)extractor.doExtract(new DefaultFileExtractionAlgorithm());
                log.info("系统License有效日期: [" + this.getDateTimeString(this.license.getStartDate().getTime()) + "--" + this.getDateTimeString(this.license.getEndDate().getTime()) + "]");
                if (!this.validate()) {
                    throw new RuntimeException("License  expired!");
                }
            } catch (Exception var4) {
                throw new RuntimeException("Invalid license!", var4);
            }
        }
    }

    public void dispose() {
        if (this.license != null) {
            this.license = null;
        }

    }

    private String getDateTimeString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (date == null) {
            date = new Date();
        }

        String DateTimeString = df.format(date);
        return DateTimeString;
    }
}
