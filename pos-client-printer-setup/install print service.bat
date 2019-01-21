cd /d %~dp0
mkdir C:\Printer_Service
robocopy PrintServiceFiles C:\Printer_Service
C:\Printer_Service/hms-client-printer-1.5.9.exe install
net start hms-client-printer-1.5.9
sc config hms-client-printer-1.5.9 start=auto

