#! /bin/bash
echo "- get data for db "
python3 hooker.py
echo "- push picture on google S3"
bash pushPictureOnS3.sh
