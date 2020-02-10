package org.mobilitydata.gtfsvalidator.usecase.notice;

import org.mobilitydata.gtfsvalidator.domain.entity.notice.ErrorNotice;

public class MissingRequiredFileNotice extends ErrorNotice {
    public MissingRequiredFileNotice(String filename) {
        super(filename, "E002",
                "Missing required file",
                "File " + filename + " is required.");
    }
}