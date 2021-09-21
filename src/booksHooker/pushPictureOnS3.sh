#! /bin/bash
cd data/pictures
for fich in `ls`
do
    curl -X POST --data-binary @$fich -H "Content-Type: image/jpeg" "https://storage.googleapis.com/upload/storage/v1/b/bucketdranse/o?uploadType=media&name=pictures/"+$fich
done
