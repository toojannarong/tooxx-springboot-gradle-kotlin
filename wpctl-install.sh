#!/usr/bin/env bash
#date:2019-06-25 14:00
LINUX="https://webplus-cn-shenzhen.oss-cn-shenzhen.aliyuncs.com/cli/wpctl-linux"
DARWIN="https://webplus-cn-shenzhen.oss-cn-shenzhen.aliyuncs.com/cli/wpctl-darwin"

if [ "${CLOUD_SHELL}" = "true" ]; then
    InstallPath="/home/shell/bin"
    mkdir -p ${InstallPath}
else
#     InstallPath="/usr/local/bin"
    InstallPath=$(pwd)
fi

download_linux() {
    if command_exists curl; then
        echo "Download wpctl from server"
        curl -# -O ${LINUX}
    else
        output "Error: curl is needed to install wpctl"
        exit 1
    fi
}

download_darwin() {
    if command_exists curl; then
        echo "Download wpctl from server"
        curl -# -O ${DARWIN}
    else
        output "Error: curl is needed to install wpctl"
        exit 1
    fi
}

command_exists() {
    command -v "$@" > /dev/null 2>&1
}

output() {
    echo -e "\033[32mwpctl install\033[0m $@"
}

auto_complete_install() {
    case ${mshell} in
    zsh)
        complete -o nospace -F ${InstallPath}/wpctl wpctl;;
    *)
        complete -C ${InstallPath}/wpctl wpctl;;
    esac
}

sh_c='sh -c'
if [ ! -w "${InstallPath}" ]; then
  if command_exists sudo; then
    sh_c='sudo sh -c'
  elif command_exists su; then
    output "Error: root is needed to install wpctl, please 'su root' then try again"
    exit 1
  else
    output "Error: root is needed to install wpctl. Unable to find either "sudo" or "su" command."
    exit 1
  fi
fi


KERNEL=$(uname -s)
mshell=$(echo $0)
TMP=$(mktemp -d "${TMPDIR:-/tmp}/wpctl.XXXXX")
PD=$(pwd)
cd ${TMP}

case ${KERNEL} in
Darwin)
    download_darwin;;
Linux)
    download_linux;;
*)
    echo "platform not supported now, please wait upgrade"
    exit 1
esac

output "installing to ${InstallPath}"
chmod +x wpctl-*
mv -f wpctl-* ${InstallPath}/wpctl
auto_complete_install
output "Done!"
cd ${PD}
rm -rf ${TMP}

