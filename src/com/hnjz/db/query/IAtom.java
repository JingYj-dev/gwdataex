package com.hnjz.db.query;

import com.hnjz.db.query.TransactionCache;
import org.hibernate.HibernateException;

public interface IAtom {
    Object execute(TransactionCache var1) throws HibernateException;

}
