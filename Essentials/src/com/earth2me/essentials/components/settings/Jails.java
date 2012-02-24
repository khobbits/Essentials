package com.earth2me.essentials.components.settings;

import com.earth2me.essentials.storage.LocationData;
import com.earth2me.essentials.storage.MapValueType;
import com.earth2me.essentials.storage.IStorageObject;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Jails implements IStorageObject
{
	@MapValueType(LocationData.class)
	private Map<String, LocationData> jails = new HashMap<String, LocationData>();
}