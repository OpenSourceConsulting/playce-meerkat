#/bin/sh
find /meerkat/agent/log/athena-meerkat-agent* -type f -mtime +15 -exec rm -rf {} \;
