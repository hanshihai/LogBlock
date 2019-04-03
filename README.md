### Log Block

This is to grep log file to block, then filter out by key words, or bad words

### usage
```
---                  usage with parameters              ---
 -f: log file path, such as -f=/va/log/log.1.log
 -p: pattern, such as -p=^\d{4}-\d{2}-\d{2}
 -k: key words for searching, such as -k=ERROR,WARNING
 -b: filtered out words, such as -b=CRON,METRIC
 -o: default output, such as -o=true
 ---                         End                         ---
```
### example
```
java Main -f=filepath -o=false -k=keyword1,keyword2
or
java Main -f=filepath -o=false -b=badword1,badword2
```