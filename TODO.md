# Project TODO
## General features
- [ ] ID3v1/ID3v2 support
- [ ] Frames type safety
- [ ] High level, functional API - no dealing with byte arrays
- [ ] ID3 tag validation
## ID3v1
...
## ID3v2
### Tag reading support
- [ ] All standard frames as case classes
  - ...
  - [ ] **TDRC** - Recording timestamp
  - [x] **APIC** - Attached picture frame
  - [x] **COMM** - Comment frame
  - [x] Text info frames
    - [x] **TALB** - Album              
    - [x] **TBPM** - Bpm                
    - [x] **TCOM** - Composers          
    - [x] **TCON** - ContentType        
    - [x] **TCOP** - Copyright          
    - [x] **TDAT** - Date               
    - [x] **TDLY** - Delay              
    - [x] **TENC** - EncodedBy          
    - [x] **TEXT** - Writers            
    - [x] **TFLT** - AudioType          
    - [x] **TIME** - Time               
    - [x] **TIT1** - ContentGroup       
    - [x] **TIT2** - Title              
    - [x] **TIT3** - Subtitle           
    - [x] **TKEY** - InitialKey         
    - [x] **TLAN** - Languages          
    - [x] **TLEN** - Length             
    - [x] **TMED** - MediaType          
    - [x] **TOAL** - OriginalAlbum      
    - [x] **TOFN** - OriginalFilename   
    - [x] **TOLY** - OriginalWriters    
    - [x] **TOPE** - OriginalArtists    
    - [x] **TORY** - OriginalReleaseDate
    - [x] **TOWN** - FileOwner          
    - [x] **TPE1** - Leaders            
    - [x] **TPE2** - Band               
    - [x] **TPE3** - Conductor          
    - [x] **TPE4** - ModifiedBy         
    - [x] **TPOS** - PartOfSet          
    - [x] **TPUB** - Publisher          
    - [x] **TRCK** - TrackNumber        
    - [x] **TRDA** - RecordingDates     
    - [x] **TRSN** - InternetRadio      
    - [x] **TRSO** - InternetRadioOwner 
    - [x] **TSIZ** - AudioSize          
    - [x] **TSRC** - ISRC               
    - [x] **TSSE** - EncoderWithSettings
    - [x] **TYER** - Year               
    - [x] **TXXX** - UserDefinedText    
- [x] Image frame materializing
- [ ] ID3 frames validation  
...
### Tag writing support
- [ ] Frame adding/removing
- [ ] Frame rewriting
- [ ] Text frames encoding fixing  
...
## Misc
- File traversing efficiency research
# Code hygiene
- Unit tests
- Strict null safety
- Custom exceptions
