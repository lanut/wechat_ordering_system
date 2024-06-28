# 这是一个运行同目录下的 .exe 文件的 PowerShell 脚本示例
# 请将以下内容保存为 .ps1 文件，例如 RunLocalApp.ps1

# 获取当前脚本所在的目录
$scriptDirectory = Split-Path -Parent $MyInvocation.MyCommand.Path

# 设置应用程序文件名（假设它在同目录下）
$exeFileName = "nginx.exe"

# 构建完整路径
$applicationPath = Join-Path -Path $scriptDirectory -ChildPath $exeFileName

# 检查应用程序是否存在
if (Test-Path $applicationPath) {
    # 运行应用程序
    Start-Process -FilePath $applicationPath
    Write-Host "应用程序已启动。"
} else {
    Write-Host "找不到指定的应用程序路径。请检查路径是否正确。"
}
