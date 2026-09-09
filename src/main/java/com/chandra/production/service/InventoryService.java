package com.chandra.production.service;

import com.chandra.production.dto.InventoryRequest;
import com.chandra.production.entity.Inventory;

public interface InventoryService {

    Inventory createInventory(InventoryRequest request);

    Inventory getInventoryById(Long id);

    Inventory getInventoryByRawMaterialId(Long rawMaterialId);

    Inventory stockIn(Long rawMaterialId, Double quantity);

    Inventory stockOut(Long rawMaterialId, Double quantity);
}