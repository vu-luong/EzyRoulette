@echo off
set "ezyclasspath=lib\*;settings;common\*;apps\common\*"
for /D %%d in (plugins\*) do (
    call set "ezyclasspath=%%ezyclasspath%%;%%d\*
)
echo classpath = %ezyclasspath%

java %1 -cp %ezyclasspath% com.tvd12.ezyfoxserver.nio.EzyNioRunner settings/config.properties