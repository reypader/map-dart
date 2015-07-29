package com.dart.data.domain;

import java.util.Date;

/**
 * Base interface for Domain Entities
 *
 * @author RMPader
 */
public interface Entity {

    String getId();

    Date getDateCreated();
}
