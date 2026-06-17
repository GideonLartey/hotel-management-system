; Lantel Hotel Management System - Inno Setup Script
; Professional Windows installer with language selection, license, branding, and components

#define MyAppName "Lantel Hotel Management System"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "Gideon Lartey (DeonLondn)"
#define MyAppURL "https://www.github.com/GideonLartey/hotel-management-system"
#define MyAppExeName "LantelHotelApp.exe"

[Setup]
AppId={{B8F2D3A4-1C5E-4F7A-9B2D-6E8F0A1C3D5E}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppVerName={#MyAppName} v{#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}/issues
AppUpdatesURL={#MyAppURL}/releases
DefaultDirName={autopf}\LantelHotel
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=no
LicenseFile=LICENSE.txt
OutputDir=installer
OutputBaseFilename=LantelHotelSetup-v{#MyAppVersion}
SetupIconFile=app-icon.ico
Compression=lzma2/ultra64
SolidCompression=yes
WizardStyle=modern
WizardSizePercent=110
WizardImageFile=wizard-image.bmp
WizardSmallImageFile=wizard-small.bmp
PrivilegesRequired=lowest
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
UninstallDisplayIcon={app}\{#MyAppExeName}
UninstallDisplayName={#MyAppName}
VersionInfoVersion={#MyAppVersion}
VersionInfoCompany={#MyAppPublisher}
VersionInfoDescription=Lantel Hotel Management System Installer
VersionInfoCopyright=Copyright (c) 2025 Gideon Lartey
ShowLanguageDialog=yes

; ---- Language Selection (shown first as a dropdown — all fully functional) ----
[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
Name: "french"; MessagesFile: "compiler:Languages\French.isl"
Name: "german"; MessagesFile: "compiler:Languages\German.isl"
Name: "spanish"; MessagesFile: "compiler:Languages\Spanish.isl"
Name: "portuguese"; MessagesFile: "compiler:Languages\Portuguese.isl"

; ---- Tasks (checkboxes during install) ----
[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"
Name: "quicklaunchicon"; Description: "Create a Quick Launch icon"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

; ---- Components (optional feature selection) ----
[Components]
Name: "main"; Description: "Lantel Hotel Application (required)"; Types: full compact custom; Flags: fixed
Name: "docs"; Description: "License and Documentation"; Types: full

; ---- Files to install ----
[Files]
Source: "publish\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion; Components: main
Source: "LICENSE.txt"; DestDir: "{app}"; Flags: ignoreversion; Components: docs

; ---- Shortcuts (with icon) ----
[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\{#MyAppExeName}"; Comment: "Launch Lantel Hotel Management System"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\{#MyAppExeName}"; Tasks: desktopicon; Comment: "Launch Lantel Hotel Management System"
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon

; ---- Post-install: option to launch ----
[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent

; ---- Install types ----
[Types]
Name: "full"; Description: "Full installation"
Name: "compact"; Description: "Compact installation"
Name: "custom"; Description: "Custom installation"; Flags: iscustom
