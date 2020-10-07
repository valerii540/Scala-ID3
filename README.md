# Scala ID3
## General features
- ID3v1/ID3v2 support
- Frames type safety
- High level, functional API - no dealing with byte arrays
- ID3 tag validation
## TODO
### ID3v1
TBD
### ID3v2
#### Tag reading support
- [ ] Standard frames
  - ...
  - [x] **UFID** - Unique file ID frame
  - [x] **USLT** - Unsynchronized lyrics frame
  - [x] **POPM** - Popularimeter frame
  - [x] **MCDI** - Music CD identifier frame
  - [x] **PRIV** - Private frame 
  - [x] **APIC** - Attached picture frame
  - [x] **COMM** - Comment frame
  - [x] URL Link frames
    - [x] **WCOM** - Commercial information link
    - [x] **WCOP** - Copyright information link
    - [x] **WOAF** - Official audio file webpage
    - [x] **WOAR** - Official artist webpage
    - [x] **WOAS** - Official audio source webpage
    - [x] **WORS** - Official internet radio webpage
    - [x] **WPAY** - Payment link
    - [x] **WPUB** - Publishers official webpage
    - [x] **WXXX** - User defined link
  - [x] Text info frames
    - [x] **TSST** - Set subtitle
    - [x] **TSOT** - Title sort order
    - [x] **TSOP** - Performer sort order
    - [x] **TSOA** - Album sort order
    - [x] **TPRO** - Produced notice
    - [x] **TMOO** - Mood
    - [x] **TMCL** - Musician credits
    - [x] **TIPL** - Involved people
    - [x] **TDRL** - Release timestamp
    - [x] **TDOR** - Original release timestamp
    - [x] **TDEN** - Encoding timestamp
    - [x] **TALB** - Album              
    - [x] **TBPM** - Bpm                
    - [x] **TCOM** - Composers          
    - [x] **TCON** - Content type        
    - [x] **TCOP** - Copyright          
    - [x] **TDAT** - Date               
    - [x] **TDLY** - Delay              
    - [x] **TENC** - Encoded by          
    - [x] **TEXT** - Writers            
    - [x] **TFLT** - Audio type          
    - [x] **TIME** - Time               
    - [x] **TIT1** - Content group       
    - [x] **TIT2** - Title              
    - [x] **TIT3** - Subtitle           
    - [x] **TKEY** - Initial key         
    - [x] **TLAN** - Languages          
    - [x] **TLEN** - Length             
    - [x] **TMED** - Media type          
    - [x] **TOAL** - Original album      
    - [x] **TOFN** - Original filename   
    - [x] **TOLY** - Original writers    
    - [x] **TOPE** - Original artists    
    - [x] **TORY** - Original release date
    - [x] **TOWN** - File owner          
    - [x] **TPE1** - Leaders            
    - [x] **TPE2** - Band               
    - [x] **TPE3** - Conductor          
    - [x] **TPE4** - Modified by         
    - [x] **TPOS** - Part of set          
    - [x] **TPUB** - Publisher          
    - [x] **TRCK** - Track number        
    - [x] **TRDA** - Recording dates     
    - [x] **TRSN** - Internet radio      
    - [x] **TRSO** - Internet radio owner 
    - [x] **TSIZ** - Audio size          
    - [x] **TSRC** - ISRC               
    - [x] **TSSE** - Encoder with settings
    - [x] **TYER** - Year               
    - [x] **TXXX** - User defined text
    - [x] **TDRC** - Recording timestamp
    - [x] **TDTG** - Tagging timestamp
- Non-standard frames
  - [x] **NCON** - frame added by MusicMatch*
  - [x] **TSOT** - frame added by iTunes*
  - [x] **TSOP** - frame added by iTunes*
  - [x] **TSOA** - frame added by iTunes*
  - [x] **TSO2** - frame added by iTunes*
  - [x] **TSOC** - frame added by iTunes*
- [x] Image frame materializing
- [ ] ID3 frames validation
- ...

\* - Only frame header with binary data
#### Tag editing support
- [ ] Frame adding/removing
- [ ] Frame rewriting
- [ ] Text frames encoding fixing
- [ ] Non-standard frames removing
- ...
### Misc
- [ ] File traversing efficiency research
- [ ] Performance testing
- [ ] Unit tests
- [x] Strict null safety
- [ ] Custom exceptions
