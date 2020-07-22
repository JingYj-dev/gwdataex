package com.hnjz.core.model;

import java.io.Serializable;
import java.util.List;

public interface IDictable extends Serializable {
    String getItemCode();

    int getIntCode();

    String getItemName();

    String getCode();

    String getRemark();

    List<? extends com.hnjz.core.model.IDictable> getChildren();
}
