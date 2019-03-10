SELECT
	temp.id,
	temp.brandname,
	temp.ip,
	temp.address,
	temp. NAME,
	temp.url,
	temp.serialnumber,
	temp.thumbNail,
	temp.region,
	temp.create_time,
	temp.taskId,
	vt.isvalid
FROM
	(
		SELECT
			c.id AS id,
			c.brandname AS brandname,
			c.ip AS ip,
			c.address AS address,
			c.`name` AS NAME,
			c.url AS url,
			r.serialnumber AS serialnumber,
			c.thumb_nail AS thumbNail,
			t.unit_name AS region,
			c.create_time,
			r.serialnumber AS taskId
		FROM
			camera c
		LEFT JOIN vsd_task_relation r ON c.id = r.camera_file_id
		LEFT JOIN ctrl_unit t ON t.unit_identity = c.region
	) temp
LEFT JOIN vsd_task vt ON temp.serialnumber = vt.serialnumber
WHERE
	1 = 1
ORDER BY
	temp.create_time DESC,
	temp. NAME DESC

	
F:\My_WorkSpace\gitee_workspace\images_bed\images\techy