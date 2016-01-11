#/bin/sh
find /peacock/agent/log/athena-peacock-agent* -type f -mtime +15 -exec rm -rf {} \;
