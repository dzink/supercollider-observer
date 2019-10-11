#!/bin/sh
dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
echo ${dir}

command="\"${dir}/sctests.sh\" -p none --extension sc --extension yaml --extension yml -w ./src -w ../../hookConfigs/core/src --class TestDh"
echo $command
eval $command
