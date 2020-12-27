package com.nandasatria.elastic.model;

import lombok.Data;

@Data
public class Shards {
	long total;
	long successful;
	long failed;
}
