#!/bin/sh

usage()
{
    echo "Usage: sc-tests-watch [options] [class=UnitTest] [watch-path=.]

Options:
    -c | --class <path>         UnitTest class to test (\"UnitTest\" by default)
    -w | --watch <path>         directory to watch for changes (leave empty to not watch)
    -e | --extension <path>     extension to watch for changes (sc by default)
    -p | --pass <command>       pass verbosity (none|full|brief)
    -f | --tempfile <path>      location of temp test file (\"/tmp/sc-tests-watch.sc\") by default.
    -n | --nodemon <command>    custom nodemon command (\"nodemon\" by default)
    -s | --sclang <command>     custom sclang command (\"sclang -D\" by default)
    "
}

class=UnitTest
tempfile=/tmp/sc-tests-watch.sc
nodemon_command="nodemon"
watch_dirs=""
# sclang_command="sclang -D"
sclang_command="cat"
pass_verbosity="default"
while [ "$1" != "" ]; do
  case $1 in
    -c | --class ) shift
      class=$1
      ;;
    -t | --temp ) shift
      tempfile=$1
      ;;
    -w | --watch ) shift
      watch_dirs="${watch_dirs}-w $1 "
      ;;
    -e | --extension ) shift
      watch_exts="${watch_exts}$1 "
      ;;
    -n | --nodemon ) shift
      nodemon_command=$1
      ;;
    -s | --sclang ) shift
      sclang_command=$1
      ;;
    -p | --passes ) shift
      pass_verbosity=$1
      ;;
    -h | --help ) usage
      exit
      ;;
    * ) usage
      exit 1
  esac
  shift
done

case ${pass_verbosity} in
  default ) pass_method=""
    ;;
  none ) pass_method=".reportPasses_(false)"
    ;;
  brief ) pass_method=".passVerbosity_(UnitTest.brief)"
    ;;
  full ) pass_method=".passVerbosity_(UnitTest.full)"
    ;;
esac

# Create the temp sc file that will run the tests.
echo "({${class}${pass_method}.runAll(); 1.wait; 1.exit}.fork)" > ${tempfile}
echo "watch ${watch_exts}"
# Generate the command and welcome message.
if [ -z "${watch_dirs}" ]
then
  command="${sclang_command} ${tempfile}"
  echo "Running Supercollider tests for ${class}"
else
  if [ -z "${watch_exts}" ]
  then
    watch_exts="-e sc"
  fi
  command="${nodemon_command} -e \"${watch_exts}\" ${watch_dirs} -x \"${sclang_command} ${tempfile}\""
  echo "Running Supercollider tests for ${class}. Watching for changes in ${watch_dir}."
fi
echo "commamd ${command}"
# Run the command.
eval "${command}"
