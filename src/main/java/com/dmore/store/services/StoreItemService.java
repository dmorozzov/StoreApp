package com.dmore.store.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmore.store.dao.StoreItemDAO;
import com.dmore.store.entity.AbstractStoreItem;

@Service
@Transactional(rollbackOn = Exception.class) 
public class StoreItemService {
	@Autowired
	StoreItemDAO storeItemDAO;

	public void saveOrUpdateItem(AbstractStoreItem candidateItem) {
		if (candidateItem == null) {
			return;
		}
		try {
			AbstractStoreItem same = storeItemDAO.findSame(candidateItem);
			if (same != null) {
				same.incrementCount(candidateItem.getCount());
				storeItemDAO.update(same);
			} else {
				storeItemDAO.save(candidateItem);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
    } 
	
    public List<AbstractStoreItem> findAll() {
    	return storeItemDAO.findAll();
    }
	
}
