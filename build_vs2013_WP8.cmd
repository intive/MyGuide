@echo off
echo ---
echo Configure VS2013
echo ---
call "%VS120COMNTOOLS%\..\..\VC\vcvarsall.bat" x86 || goto error

set CURRENT_DIR=%CD%
set EnableNuGetPackageRestore="true"

echo ---
echo Building
echo ---
cd %CURRENT_DIR%\MyGuide
msbuild MyGuide.sln /t:rebuild /p:Configuration="Release" || goto error
msbuild MyGuide.sln /t:rebuild /p:Configuration="Debug" || goto error

goto end

:error
echo ---
echo --- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ---
echo ---
echo ---
echo ------------- FAILED TO BUILD: %MODULE_NAME%
echo ---
echo ---
echo --- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ---
echo ---
cd %CURRENT_DIR%
exit /b 1

:end